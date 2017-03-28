[Virtual-Meeting-Room](http://localhost:8080/)

This is a sample application to enable virtual meeting via text chat, video chat and audio chat.

#### To build this project use
    mvn install

#### To run this project with maven use
    mvn spring-boot:run

#### Application end points
    Testing end points
        http://localhost:8080/
        http://localhost:8080/hello
        http://localhost:8080/html/index.html
        ws://localhost:8080/vmr
    
    End point to start chatting application
        http://localhost:8080/html/chat.html?u=Varun

#### Usage
    Open following two links and start text chat between Varun and Nitin
         http://localhost:8080/html/chat.html?u=Varun
         http://localhost:8080/html/chat.html?u=Nitin

#### Java Script library Used
    jquery-3.1.1.js
    adapter.js
    https://github.com/allmarkedup/purl/edit/master/purl.js
    
#### References
    https://github.com/webrtc/adapter
    https://webrtc.github.io/samples/
    https://codelabs.developers.google.com/codelabs/webrtc-web/#2
    https://www.tutorialspoint.com/webrtc/webrtc_signaling.htm
    https://www.safaribooksonline.com/library/view/webrtc-cookbook/9781783284450/ch01s05.html
    https://www.html5rocks.com/ko/tutorials/webrtc/basics/