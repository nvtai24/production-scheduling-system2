// Base64 encoding and decoding functions
function encodeBase64(str) {
    return btoa(str);
}

function decodeBase64(str) {
    return atob(str);
}

// Khi trang được tải, kiểm tra localStorage để tự động điền username và password
window.onload = function() {
    const rememberMeCheckbox = document.getElementById('remember-me');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');

    // Kiểm tra xem có username và password được lưu trong localStorage không
    const savedUsername = localStorage.getItem('rememberedUsername');
    const savedPassword = localStorage.getItem('rememberedPassword');

    if (savedUsername && savedPassword) {
        usernameInput.value = savedUsername;
        passwordInput.value = decodeBase64(savedPassword); // Giải mã mật khẩu
        rememberMeCheckbox.checked = true;  // Đánh dấu checkbox Remember Me
    }

    // Khi người dùng bấm submit form, lưu username và password nếu checkbox Remember Me được chọn
    document.querySelector('.form').addEventListener('submit', function() {
        if (rememberMeCheckbox.checked) {
            localStorage.setItem('rememberedUsername', usernameInput.value);
            localStorage.setItem('rememberedPassword', encodeBase64(passwordInput.value)); // Mã hóa mật khẩu
        } else {
            localStorage.removeItem('rememberedUsername');
            localStorage.removeItem('rememberedPassword');
        }
    });
}