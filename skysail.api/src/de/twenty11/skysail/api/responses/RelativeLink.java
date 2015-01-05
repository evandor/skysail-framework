package de.twenty11.skysail.api.responses;

import org.restlet.Context;

import de.twenty11.skysail.api.config.Configuration;

public class RelativeLink extends Link {

    public RelativeLink(String href, String title) {
        super(Configuration.getHome() + "/" + href, title);
    }

    /**
     * for example getContext(), "mail/accounts/?media=htmlform", "new Account"
     */
    // TODO make sure href does not start with "/"
    public RelativeLink(Context context, String href, String title) {
        super(context, Configuration.getHome() + "/" + href, title);
    }

}
