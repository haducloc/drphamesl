package com.drphamesl.controllers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.Out;
import com.appslandia.common.utils.NumberUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ValueUtils;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.Model;
import com.appslandia.plum.base.PathParams;
import com.appslandia.plum.base.PrefCookieHandler;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.drphamesl.entities.VocabList;
import com.drphamesl.models.FlashcardModel;
import com.drphamesl.models.VocabIndexModel;
import com.drphamesl.models.VocabListModel;
import com.drphamesl.models.VocabTestModel;
import com.drphamesl.services.VocabListService;
import com.drphamesl.services.VocabService;
import com.drphamesl.services.VocabServiceUtil;
import com.drphamesl.utils.AccountUtils;
import com.drphamesl.utils.NextTypes;
import com.drphamesl.utils.ShareTypes;
import com.drphamesl.utils.TestOrders;
import com.drphamesl.utils.VocabOrders;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller
public class VocabController {

	@Inject
	protected VocabService vocabService;

	@Inject
	protected VocabServiceUtil vocabServiceUtil;

	@Inject
	protected VocabListService vocabListService;

	@Inject
	protected PrefCookieHandler prefCookieHandler;

	@HttpGet
	@EnableEtag
	public ActionResult index(RequestAccessor request, HttpServletResponse response) throws Exception {
		// VocabLists
		List<VocabList> vocabLists = this.vocabListService.getVocabLists();
		VocabIndexModel model = new VocabIndexModel();

		if (AccountUtils.isAdminEmail(request.getRemoteUser())) {
			model.setVocabLists(vocabLists);
		} else {
			model.setVocabLists(vocabLists.stream().filter(l -> l.getShareType() == ShareTypes.ON_PUBLIC).collect(Collectors.toList()));
		}

		request.storeModel(model);
		return JspResult.DEFAULT;
	}

	@HttpGet
	@EnableEtag
	@PathParams("/{pk}-{title_path}")
	public ActionResult list(RequestAccessor request, HttpServletResponse response, String pk, String title_path) throws Exception {
		// PK
		Out<Boolean> isPkInt = new Out<Boolean>();
		int pkInt = NumberUtils.parseInt(pk, isPkInt);
		request.assertNotFound(!isPkInt.val() || pkInt > 0);

		// VocabLists
		List<VocabList> vocabLists = this.vocabListService.getVocabLists();
		VocabList vocabList = vocabLists.stream().filter(l -> (pkInt > 0) ? (l.getVocabListId() == pkInt) : l.getSpk().equals(pk)).findFirst().orElse(null);

		request.assertNotFound(vocabList != null);
		request.assertNotFound(vocabList.getShareType() == ((pkInt > 0) ? ShareTypes.ON_PUBLIC : ShareTypes.ON_PRIVATE));

		if (!vocabList.getTitle_path().equals(title_path)) {
			return new RedirectResult("list", "vocab").status(HttpServletResponse.SC_MOVED_PERMANENTLY).query("pk", pk).query("title_path",
					vocabList.getTitle_path());
		}

		VocabListModel model = new VocabListModel();
		model.setVocabList(vocabList);
		model.setVocabs(this.vocabServiceUtil.getVocabs(vocabList.getTag()));

		request.storeModel(model);
		return JspResult.DEFAULT;
	}

	void initFlashcard(RequestAccessor request, FlashcardModel model) {
		model.setVocabOrders(VocabOrders.createList(request.getResources()));
		model.setNextTypes(NextTypes.createList(request.getResources()));
	}

	@Authorize(appscope = true)
	@HttpGet
	@PathParams("/{pk}-{title_path}")
	public ActionResult flashcard(RequestAccessor request, HttpServletResponse response, String pk, String title_path, @Model FlashcardModel model)
			throws Exception {
		// PK
		Out<Boolean> isPkInt = new Out<Boolean>();
		int pkInt = NumberUtils.parseInt(pk, isPkInt);
		request.assertNotFound(!isPkInt.val() || pkInt > 0);

		// VocabLists
		List<VocabList> vocabLists = this.vocabListService.getVocabLists();
		VocabList vocabList = vocabLists.stream().filter(l -> (pkInt > 0) ? (l.getVocabListId() == pkInt) : l.getSpk().equals(pk)).findFirst().orElse(null);

		request.assertNotFound(vocabList != null);
		request.assertNotFound(vocabList.getShareType() == ((pkInt > 0) ? ShareTypes.ON_PUBLIC : ShareTypes.ON_PRIVATE));

		if (!vocabList.getTitle_path().equals(title_path)) {
			return new RedirectResult("list", "vocab").status(HttpServletResponse.SC_MOVED_PERMANENTLY).query("pk", pk).query("title_path",
					vocabList.getTitle_path());
		}

		// Default Values
		if (model.getVocabOrder() == null) {
			model.setVocabOrder(request.getIntPref("flashcard.vocabOrder", 0));
		}
		if (model.getNextType() == null) {
			model.setNextType(request.getPref("flashcard.nextType"));
		}

		model.setVocabOrder(VocabOrders.toVocabOrder(model.getVocabOrder()));
		model.setNextType(NextTypes.toNextType(model.getNextType()));
		model.setNextInMs(NextTypes.toNextInMs(model.getNextType()));

		if (model.getVocabOrder() == VocabOrders.ALPHABETICAL) {
			model.setIndex(ValueUtils.valueOrMin(model.getIndex(), 1));
		}

		// Save cookies
		if (!ObjectUtils.equals_str(request.getPref("flashcard.vocabOrder"), model.getVocabOrder())
				|| !Objects.equals(request.getPref("flashcard.nextType"), model.getNextType())) {

			prefCookieHandler.savePrefCookie(request, response, pc -> {
				pc.set("flashcard.vocabOrder", model.getVocabOrder());
				pc.set("flashcard.nextType", model.getNextType());
			});
		}

		// Generate Card
		vocabService.generateCard(vocabList.getTag(), model);
		model.setVocabList(vocabList);

		request.getModelState().clearErrors();
		initFlashcard(request, model);

		request.storeModel(model);
		return JspResult.DEFAULT;
	}

	void initTest(RequestAccessor request, VocabTestModel model) {
		model.setTestOrders(TestOrders.createList(request.getResources()));
		model.setNextTypes(NextTypes.createList(request.getResources()));
	}

	@Authorize(appscope = true)
	@HttpGet
	@PathParams("/{pk}-{title_path}")
	public ActionResult test(RequestAccessor request, HttpServletResponse response, String pk, String title_path, @Model VocabTestModel model)
			throws Exception {
		// PK
		Out<Boolean> isPkInt = new Out<Boolean>();
		int pkInt = NumberUtils.parseInt(pk, isPkInt);
		request.assertNotFound(!isPkInt.val() || pkInt > 0);

		// VocabLists
		List<VocabList> vocabLists = this.vocabListService.getVocabLists();
		VocabList vocabList = vocabLists.stream().filter(l -> (pkInt > 0) ? (l.getVocabListId() == pkInt) : l.getSpk().equals(pk)).findFirst().orElse(null);

		request.assertNotFound(vocabList != null);
		request.assertNotFound(vocabList.getShareType() == ((pkInt > 0) ? ShareTypes.ON_PUBLIC : ShareTypes.ON_PRIVATE));

		if (!vocabList.getTitle_path().equals(title_path)) {
			return new RedirectResult("list", "vocab").status(HttpServletResponse.SC_MOVED_PERMANENTLY).query("pk", pk).query("title_path",
					vocabList.getTitle_path());
		}

		// Default Values
		if (model.getTestOrder() == null) {
			model.setTestOrder(request.getIntPref("vocabTest.testOrder", 0));
		}
		if (model.getNextType() == null) {
			model.setNextType(request.getPref("vocabTest.nextType"));
		}

		model.setTestOrder(TestOrders.toTestOrder(model.getTestOrder()));
		model.setNextType(NextTypes.toNextType(model.getNextType()));
		model.setNextInMs(NextTypes.toNextInMs(model.getNextType()));

		if (model.getTestOrder() == TestOrders.PRE_DEFINED) {
			model.setIndex(ValueUtils.valueOrMin(model.getIndex(), 1));
		}

		// Save cookies
		if (!ObjectUtils.equals_str(request.getPref("vocabTest.testOrder"), model.getTestOrder())
				|| !Objects.equals(request.getPref("vocabTest.nextType"), model.getNextType())) {

			prefCookieHandler.savePrefCookie(request, response, pc -> {
				pc.set("vocabTest.testOrder", model.getTestOrder());
				pc.set("vocabTest.nextType", model.getNextType());
			});
		}

		// Generate Test
		vocabService.generateTest(vocabList.getTag(), model);
		model.setVocabList(vocabList);

		request.getModelState().clearErrors();
		initTest(request, model);

		request.storeModel(model);
		return JspResult.DEFAULT;
	}
}
