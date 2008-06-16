package pages.components;

import wicket.markup.ComponentTag;
import wicket.markup.html.WebComponent;
import wicket.model.IModel;

@SuppressWarnings("serial")
public class StaticImage extends WebComponent {

    public StaticImage(String id, IModel model) {
        super(id, model);
    }

    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        checkComponentTag(tag, "img");
        tag.put("src", getModelObjectAsString());
    }
}
