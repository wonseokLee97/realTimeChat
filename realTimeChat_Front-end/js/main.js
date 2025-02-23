// main.js
import ChatApp from './ChatApp.js';

$(document).ready(() => {
    $("#new-message-alert").on("click", function () {
        $("#chatting").animate({ scrollTop: $("#chatting")[0].scrollHeight }, 300);
        $(this).fadeOut(200);
    });

    const app = new ChatApp();
    app.init();
});
