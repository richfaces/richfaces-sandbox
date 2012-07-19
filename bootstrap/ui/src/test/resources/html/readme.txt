The HTML files in this folder are meant to assit in developing the standalone javascript widgets.

Some borwsers (Mozilla Firefox and Google Chrome) have a security model that prevents the LESS javascript compiler from loading local file resoures.  To overcome this, run a simple local file server out of the sandbox/bootstrap folder, such as:

`python -m SimpleHTTPServer`

Then access your files via localhost:
http://localhost:8000/ui/src/test/resources/html/
