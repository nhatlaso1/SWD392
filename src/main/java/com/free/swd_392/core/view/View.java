package com.free.swd_392.core.view;

public interface View {

    interface Info extends View, FullIncludeExclude {
    }

    interface Details extends Info {
    }

    interface Include {
        interface Create {
        }

        interface Update {
        }
    }

    interface Exclude {
        interface Create {
        }

        interface Update {
        }
    }

    interface FullIncludeExclude extends
            Include.Create, Exclude.Create,
            Include.Update, Exclude.Update {
    }
}
