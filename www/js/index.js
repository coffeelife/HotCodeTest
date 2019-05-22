/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },

    bindEvents: function() {
        //页面布局
        window.addEventListener('load', function (){
            var sizeA = $("html").width()*16/320;
            $("html").css("fontSize",sizeA + "px");

            var sizeB = $("html").attr("style").replace("font-size:","").replace("px;","").trim() * 1.0;
            if(sizeA != sizeB) {
                sizeA = sizeA * sizeA /sizeB;
                $("html").css("fontSize", sizeA + "px");
            }

            if($("html").width() != $(".page").width()){
                sizeA = sizeA * $("html").width() /$(".page").width();
                $("html").css("fontSize", sizeA + "px");
            }
        }, false);

        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
        document.addEventListener("resume", function(){
            chcp.fetchUpdate(function(error, data) {
                if(!error) {
                    console.log("updateing");
                    chcp.installUpdate(function(error) {
                        console.log("finish");
                    });
                } else {
                    console.log("isnew");
                }
            })
        });
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
        this.receivedEvent('deviceready');
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();