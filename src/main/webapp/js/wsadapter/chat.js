$(document).ready(function(){
    var chatForm = $("#chat_form")
    $("#chat_submit_btn").click(function( event ) {
      var message = $("#chat_input").val();
      var msg = JSON.stringify({"type" : "chatmessage", "txt" : message});
      sendDataMessage(msg);
    });

    function onRoomReceived(room) {
        var st = $("#status")
        st.html("Now, if somebody wants to join you, should use this link: <a href=\""+window.location.href+"?room="+room+"\">"+window.location.href+"?room="+room+"</a>");
    };

    function onPrivateMessageReceived(txt) {
        var t = $("#chat").html();
        t += "<br>" + txt;
        $("#chat").html(t);
    }
});

