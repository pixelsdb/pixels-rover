if (window.top !== window.self) {
    alert('Please login again');
    window.top.location = window.location
};

$(function () {
    validateRule()
    $('.imgcode').click(function () {
        var url = "/captcha/captchaImage";
        $(".imgcode").attr("src", url);
    });
});

$.validator.setDefaults({
    submitHandler: function () {
        login();
    }
});

function login() {
    var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    $.ajax({
        type: "post",
        url: "login",
        data: {
            "username": username,
            "password": password,
            "validateCode": validateCode
        },
        success: function (r) {
            if (r.code == 200) {
                location.href = 'home';
            } else {
                $('.imgcode').click();
                $(".code").val("");
                $.modal.msg(r.msg);
            }
        }
    });
}

function validateRule() {
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: "Please enter your username",
            },
            password: {
                required: "Please enter your password",
            }
        }
    })
}