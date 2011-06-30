import markdown

f = open('testfile.txt', 'r')

"""
    this is where the extension is loaded. For this to work the extensions
    filename has to start with 'mdx_' in this case:
        mdx_singleheader.py
"""
md = markdown.Markdown(extensions=['singleheader'])
out = md.convert(f.read())

output = open('output.html', 'w')
output.write(out)
