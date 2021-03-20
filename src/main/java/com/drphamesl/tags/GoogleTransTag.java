package com.drphamesl.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.appslandia.common.utils.URLEncoding;
import com.appslandia.plum.tags.Attribute;
import com.appslandia.plum.tags.Tag;
import com.appslandia.plum.tags.UITagBase;
import com.appslandia.plum.utils.HtmlUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "googleTrans", bodyContent = "scriptless")
public class GoogleTransTag extends UITagBase {

	private String text;
	private String _href;

	@Override
	protected String getTagName() {
		return "a";
	}

	@Override
	protected void initTag() throws JspException, IOException {
		this._href = "https://translate.google.com/?op=translate&sl=en&tl=vi&text=" + URLEncoding.encodeParam(this.text);
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.escAttribute(out, "href", this._href);
		HtmlUtils.attribute(out, "target", "_blank");

		if (this.hidden)
			HtmlUtils.hidden(out);

		if (this.datatag != null)
			HtmlUtils.escAttribute(out, "data-tag", this.datatag);
		if (this.clazz != null)
			HtmlUtils.attribute(out, "class", this.clazz);
		if (this.style != null)
			HtmlUtils.attribute(out, "style", this.style);
		if (this.title != null)
			HtmlUtils.escAttribute(out, "title", this.title);
	}

	@Override
	protected boolean hasBody() {
		return true;
	}

	@Override
	protected void writeBody(JspWriter out) throws JspException, IOException {
		if (this.jspBody != null) {
			this.jspBody.invoke(out);
		} else {
			out.write(this.getRequestContext().escCt("label.translate"));
		}
	}

	@Attribute(required = true, rtexprvalue = true)
	public void setText(String text) {
		this.text = text;
	}
}
