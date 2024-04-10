$(function() {
    $('.imgcode').click(function() {
        var url = "/captcha/captchaImage";
        $(".imgcode").attr("src", url);
    });
});