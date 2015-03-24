function addFadingMessage(msg, target) {
	$(target).text(msg);
	$(target).fadeIn().delay(1000).fadeOut();
}

function escape (key, val) {
    if (typeof(val)!="string") return val;
    return val
      .replace(/[\\]/g, '\\\\')
      .replace(/[\b]/g, '\\b')
      .replace(/[\f]/g, '\\f')
      .replace(/[\n]/g, '')
      .replace(/[\r]/g, '\\r')
      .replace(/[\t]/g, '\\t')
    ; 
}