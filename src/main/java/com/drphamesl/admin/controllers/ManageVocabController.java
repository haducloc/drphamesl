package com.drphamesl.admin.controllers;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.appslandia.common.base.Bind;
import com.appslandia.common.base.MemoryStream;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.FileNameUtils;
import com.appslandia.common.utils.MimeTypes;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.common.utils.StringFormat;
import com.appslandia.common.utils.TagList;
import com.appslandia.common.utils.TagUtils;
import com.appslandia.common.utils.ValueUtils;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.ExceptionHandler;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.HttpGetPost;
import com.appslandia.plum.base.HttpPost;
import com.appslandia.plum.base.Model;
import com.appslandia.plum.base.ModelBinder;
import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.PagerModel;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.base.TagCookieHandler;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.appslandia.plum.results.TextFileResult;
import com.drphamesl.entities.Vocab;
import com.drphamesl.formatters.Formatters;
import com.drphamesl.models.LoadVocabResult;
import com.drphamesl.models.ManageVocabIndexModel;
import com.drphamesl.models.MyTagsModel;
import com.drphamesl.services.VocabLoader;
import com.drphamesl.services.VocabService;
import com.drphamesl.utils.PreTagUtils;
import com.drphamesl.utils.TimeUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(value = "manage-vocab", module = Modules.ADMIN)
@Authorize(appscope = true, policies = AdminPolicy.ADMIN)
public class ManageVocabController {

	static final int PAGE_SIZE = 25;
	public static final String VOCAB_TAG_LIST = "vocabTagList";

	@Inject
	protected AppLogger logger;

	@Inject
	protected ModelBinder modelBinder;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected VocabService vocabService;

	@Inject
	protected VocabLoader vocabLoader;

	@Inject
	protected TagCookieHandler tagCookieHandler;

	final Supplier<TagList> tagListNewer = () -> new TagList(PreTagUtils.PRE_TAGS);

	void prepareIndex(RequestAccessor request) throws Exception {
		tagCookieHandler.getTagList(request, VOCAB_TAG_LIST, tagListNewer);
	}

	@HttpGet
	public ActionResult index(RequestAccessor request, HttpServletResponse response, @Model ManageVocabIndexModel model) throws Exception {
		model.setPageIndex(ValueUtils.valueOrMin(model.getPageIndex(), 1));

		// VOCABS
		List<Vocab> list = this.vocabService.query(model.getQuery(), model.getPageIndex(), PAGE_SIZE, model.getRecordCount());
		model.setVocabs(list);
		model.setPagerModel(new PagerModel(model.getPageIndex(), model.getRecordCount().val(), PAGE_SIZE));

		// Active Tags
		if (model.getRecordCount().val() > 0 && model.isQueryTag()) {
			tagCookieHandler.saveTag(request, response, model.getQuery(), VOCAB_TAG_LIST, tagListNewer);
		}

		request.storeModel(model);

		prepareIndex(request);
		return JspResult.DEFAULT;
	}

	void prepareEdit(RequestAccessor request) throws Exception {
		tagCookieHandler.getTagList(request, VOCAB_TAG_LIST, tagListNewer);
	}

	@HttpGetPost
	public ActionResult edit(RequestAccessor request, HttpServletResponse response, Integer vocabId) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			Vocab model = null;

			if (vocabId == null) {
				model = new Vocab();
				model.setTags(request.getParamOrNull("tags"));
			} else {
				model = this.vocabService.findByPk(vocabId);
				request.assertNotNull(model);
			}

			request.storeModel(model);
			prepareEdit(request);
			return JspResult.DEFAULT;
		}

		// POST
		Vocab model = new Vocab();
		modelBinder.bindModel(request, model);

		// Parse vocabId
		if (model.getVocabId() == null && request.getModelState().isValid("vocabId")) {
			String vid = request.findParam("vocabId");
			model.setVocabId(request.getRequestContext().parseOrNull(vid, Integer.class));
		}

		request.getModelState().remove("timeCreated");
		if (!request.getModelState().isValid()) {

			request.storeModel(model);
			prepareEdit(request);
			return JspResult.DEFAULT;
		}

		boolean isNew = (model.getVocabId() == null);
		try {
			// Remove?
			if (request.isRemoveAction()) {
				vocabService.remove(model.getVocabId());

				request.getMessages().addNotice(request.res("record.deleted_successfully", request.res("vocab")));
				return new RedirectResult("index");
			}

			// New
			if (isNew) {
				model.setTimeCreated(TimeUtils.nowAtGMT7());
			}

			// Active Tags
			tagCookieHandler.saveDbTags(request, response, model.getTags(), VOCAB_TAG_LIST, tagListNewer);

			// Save
			this.vocabService.save(model);
			request.getMessages().addNotice(request.res("record.saved_successfully", request.res("vocab")));

			if (isNew) {
				return new RedirectResult("edit").query("tags", TagUtils.toDispTags(model.getTags()));
			} else {
				return new RedirectResult("edit");
			}

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			prepareEdit(request);
			return JspResult.DEFAULT;
		}
	}

	@HttpPost
	public LoadVocabResult load(RequestAccessor request, @Bind(fmt = Formatters.WORDS) String words) throws Exception {
		request.assertValidModel();

		LoadVocabResult result = new LoadVocabResult();
		result.setWords(words);

		Vocab vocab = this.vocabService.findByWords(words);
		if (vocab != null) {
			result.setMessage(request.res("manage_vocab_edit.vocab_added_already", words));

			ModelUtils.copyProps(result, vocab, "vocabId", "definitions");
			result.setTags(TagUtils.toDispTags(vocab.getTags()));
			return result;
		}

		vocab = new Vocab();
		vocab.setWords(words);

		vocabLoader.load(vocab);
		result.setDefinitions(vocab.getDefinitions());

		return result;
	}

	@HttpGet
	@EnableEtag
	public ActionResult mytags(RequestAccessor request, HttpServletResponse response, @Model MyTagsModel model) throws Exception {
		List<String> tags = this.vocabService.getVocabTags();
		model.setMytags(tags);

		request.storeModel(model);
		return JspResult.DEFAULT;
	}

	@HttpGet
	public ActionResult export(RequestAccessor request, HttpServletResponse response, @Bind(fmt = Formatter.TAG) String tag) throws Exception {
		request.assertNotNull(tag);

		// VOCABS
		List<Vocab> vocabs = vocabService.queryByTag(tag);
		final String[] columnLabels = new String[] { "vocab", "definitions", "tags" };

		// CSV
		CSVFormat format = CSVFormat.EXCEL.builder().setHeader(columnLabels).build();
		MemoryStream ms = new MemoryStream();

		try (Writer out = new BufferedWriter(new OutputStreamWriter(ms, StandardCharsets.UTF_8))) {
			try (CSVPrinter csvOut = new CSVPrinter(out, format)) {

				for (Vocab vocab : vocabs) {
					csvOut.printRecord(vocab.getWords(), vocab.getDefs1Line(), vocab.getTags());
				}
			}
		}

		String content = ms.toString(StandardCharsets.UTF_8);
		String fileName = StringFormat.fmt("export-vocabs-{}.csv", tag);

		return new TextFileResult(content, FileNameUtils.toFileNameNow(fileName), MimeTypes.APP_CSV);
	}
}
