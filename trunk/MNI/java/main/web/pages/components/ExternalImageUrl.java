package pages.components;

import wicket.AttributeModifier;
import wicket.markup.ComponentTag;
import wicket.markup.html.WebComponent;
import wicket.model.Model;

public class ExternalImageUrl extends WebComponent {

	private static final long serialVersionUID = 1L;

	public ExternalImageUrl(String id, String imageUrl) {
	    super(id);
	    add(new AttributeModifier("src", true, new Model(imageUrl)));
	    setVisible(!(imageUrl==null || imageUrl.equals("")));
	}
	
	protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            checkComponentTag(tag, "img");
        }
}
