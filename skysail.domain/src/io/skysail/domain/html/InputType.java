package io.skysail.domain.html;

// http://www.wufoo.com/html5/
public enum InputType {
    // HTML input types
    TEXT, PASSWORD, CHECKBOX, RADIO, SUBMIT, RESET, FILE, HIDDEN, IMAGE, BUTTON,
    // textarea tag
    TEXTAREA, TAGS, READONLY, EMAIL, URL, RANGE, TEL, SEARCH, COLOR, NUMBER, MULTISELECT,

    DATE, DATETIME_LOCAL("datetime-local"), MONTH, WEEK, TIME,
    
    MARKDOWN_EDITOR, 
    TRIX_EDITOR;
    
    private String value;

    private InputType() {
        value = this.name();
    }
    
    private InputType(String value) {
        this.value = value;
    }
}
