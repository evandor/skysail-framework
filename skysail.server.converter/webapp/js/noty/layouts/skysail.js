$.noty.layouts.skysail = {
    name     : 'skysail',
    options  : { // overrides options

    },
    container: {
        object  : '<ul id="noty_skysail_layout_container" />',
        selector: 'ul#noty_skysail_layout_container',
        style   : function() {
            $(this).css({
                top          : 104,
                right        : 15,
                position     : 'fixed',
                width        : '310px',
                height       : 'auto',
                margin       : 0,
                padding      : 0,
                listStyleType: 'none',
                zIndex       : 10000000
            });

            if(window.innerWidth < 600) {
                $(this).css({
                    right: 5
                });
            }
        }
    },
    parent   : {
        object  : '<li />',
        selector: 'li',
        css     : {}
    },
    css      : {
        display: 'none',
        width  : '310px'
    },
    addClass : ''
};