$(function() {
    $(".btn[type='submit']").on('click', function(e) {
        e.preventDefault();
        $(this).attr('disabled', '');
        $(this).parents('form').submit();
    });
});