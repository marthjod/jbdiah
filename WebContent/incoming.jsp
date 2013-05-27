<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="de.jbdiah.util.Constants,org.apache.log4j.Logger" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="header.jsp" %>
<title>Incoming</title>
</head>

<%  
	String server = request.getServerName();
	if ( "127.0.0.1".equals(server)) {
		server = "localhost";
	}
	final String context = "http://" + server + ":" + request.getServerPort() +  request.getContextPath();
	final String showUrl = context + Constants.SHOW_VIEW_PATH;
	final String postBibUrl = context + Constants.POST_BIB;
	final String postMdUrl = context + Constants.POST_MARKDOWN;	
	final String convertUrl = context + Constants.CONVERT_REQUEST;
	final String retrievalUrl = context + Constants.RETRIEVAL_REQUEST;
%>


<body onload="javascript:document.getElementById('md-box').style.border = 'solid ' + get_random_color() + ' 3px';document.getElementById('bib-box').style.border = 'solid ' + get_random_color() + ' 3px';return false;">

	<div class="drop-box" id="md-box" dropzone="move s:text"  ondrop="drop(event, this);">
		<div class="container">
			<img src="http://cargo.dcurt.is/markdown_mark_large.png" alt="Markdown" />
		</div>
	</div>
	
	<div class="drop-box" id="bib-box" dropzone="move s:text"  ondrop="drop(event, this);">
		<div class="container">
			<img src="http://upload.wikimedia.org/wikipedia/commons/thumb/2/21/BibTeX_logo.png/799px-BibTeX_logo.png" alt="BibTeX" />
		</div>	
	</div>
	
	<div>
		<button id="convert"  class="button" onclick="convert();">Launch</button>
	</div>
	
	<div id="response" class="response">
	</div>
	
	<div id="status" class="status">
	</div>
	
<%@ include file="footer.jsp" %>

<script type="text/javascript">
var showUrl = '<%= showUrl %>';
var postBibUrl = '<%= postBibUrl %>';
var postMdUrl = '<%= postMdUrl %>';
var convertUrl = '<%= convertUrl %>';
var retrievalUrl = '<%= retrievalUrl %>';
var mdbox = document.getElementById('md-box');
var bibbox = document.getElementById('bib-box');
var filebox = document.getElementById('file-box');
var run = 0; 

function get_random_color() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.round(Math.random() * 15)];
    }
    return color;
}

function convert() {
	
	var button = document.getElementById('convert');
	button.innerHTML = 'Launched...'
	button.setAttribute('disabled', 'disabled');
	
	var status = document.getElementById('status');
	
	var xhr = new XMLHttpRequest();
	xhr.open("PUT", convertUrl, true);
	
	xhr.onreadystatechange = function () {
		if (xhr.readyState === 4) {
			document.getElementById('response').innerHTML = xhr.responseText;
			
			if (xhr.status === 200) {
				status.style.backgroundColor = '#9fa';
				status.innerHTML = 'Status: OK';
				window.location = retrievalUrl;				
			} else {
				status.style.backgroundColor = '#f52';
				status.innerHTML = 'Status: Fail';
			}
		}
	};
	
	xhr.send();
	
}

function feedback(box, msg, timeout) {
	if (box && box.hasOwnProperty('id')) {
		document.getElementById(box.id).innerHTML = msg;
		window.setTimeout('refresh()', timeout);
	} 
}

function noopHandler(evt) {
	  evt.stopPropagation();
	  evt.preventDefault();
}

function refresh() {
	window.location = showUrl;
}

function drop(evt, box) {
	
	if (run === 0) {
	
		evt.stopPropagation();
		evt.preventDefault();
		
		var postUrl = '';
		
		if (box && mdbox && box.hasOwnProperty('id') && mdbox.id === box.id) {
			postUrl = postMdUrl;
		} else if (box && bibbox && box.hasOwnProperty('id') && bibbox.id === box.id) {
			postUrl = postBibUrl;
		} 
		 
		var file = evt.dataTransfer.files[0];
	
		var handle_files = function () {
			var reader = new FileReader();
			 
			// init the reader event handlers
			reader.onload = function (evt, url) {
				var content = evt.target.result;
				var xhr = new XMLHttpRequest();
				
				xhr.open("POST", postUrl, true);
				
				xhr.onreadystatechange = function() {
					if (xhr.readyState === 4) {
						if (xhr.status === 200) {			
							feedback(box, 'Sent to server.', 500);
						} else if (xhr.status === 400) {
							feedback(box, 'Bad request (wrong file format?)', 2000);
						} else {
							feedback(box, 'Something went wrong (HTTP status code is '+xhr.status+').', 2000);
						}
					}
				};
				xhr.send(content);			
			};
			 
			// begin the read operation
			reader.readAsDataURL(file);			
		}();
		
		// global flag
		run = 1;
	}
} 


// init event handlers
mdbox.addEventListener("dragenter", noopHandler, false);
mdbox.addEventListener("dragexit", noopHandler, false);
mdbox.addEventListener("dragover", noopHandler, false);
mdbox.addEventListener("drop", drop, false);

bibbox.addEventListener("dragenter", noopHandler, false);
bibbox.addEventListener("dragexit", noopHandler, false);
bibbox.addEventListener("dragover", noopHandler, false);
bibbox.addEventListener("drop", drop, false);
</script>

</body>
</html>