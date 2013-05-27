jbdiah
======

Minimal user-friendly web frontend for Markdown-/BibTeX-to-PDF conversion service

Description
===========

A stripped-down web frontend provides two HTML5 drop zones for uploading a Markdown or BibTeX file, respectively.
The webapp then calls a server-side service (e.g., `make` or a conversion toolchain) producing a result file 
handed off to the browser.

Example scenario
----------------

After uploading new source files (.md, .bib), the Markdown file gets converted by 
[Pandoc](http://johnmacfarlane.net/pandoc/), then (together with the BibTeX file) fed to XeLaTeX, 
subsequently converted to a PDF.
If the conversion was successful (`make` exit status determines Ajax call return code), the browser 
automatically redirects to the retrieval URL which will trigger the result file to be downloaded/displayed.

User-friendly
-------------

The user has to write only minimal markup (Markdown) and may manage her BibTeX entries in a GUI, thus
keeping potential distractions to a minimum.

When needed, content source files are simply dragged & dropped onto the _jbdiah_ web page. After 
a short time (conversion plus download), the compiled, LaTeX-template-powered PDF is returned. 

(Of course, the LaTeX template probably has to be customized. User errors wrt encoding, 
syntax errors in raw inline LaTeX vel sim. are additional aspects but outside of this webapp's scope.)


Security
--------

Valid logins (user names and password hashes) have to be pre-generated and are stored in an XML file
written and read using the [XStream](http://xstream.codehaus.org/) serialization library.

All page requests run through the _AuthFilter_, which checks for appropriate cookies set 
after successful login.

