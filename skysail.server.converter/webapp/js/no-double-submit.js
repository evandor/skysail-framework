$(function() {
    $(".btn[type='submit']").on('click', function() {
        $(this).attr('disabled', '');
    });
});