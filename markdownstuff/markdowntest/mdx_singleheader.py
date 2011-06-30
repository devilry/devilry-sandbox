import re
import markdown

"""
    this is a simple extension to only allow atx-h2-headers in markdown,
    that is headers like this:
        ## this is a header ##
        ## and this i another header
    while h1, h3, h4 etc are simply converted to plain text..

    to do this as easily as possible this extension is a 'preprocessor'
    that means that it can manopulate the markdown-code before its converted
    to html by markdown.
"""
class OnlyOneHeader(markdown.preprocessors.Preprocessor):
    def run(self, lines):
        findheader_regex = r'(^|\n)(?P<level>\#{1,6})(?P<header>.*?)\#*(\n|$)'
        findheaders = re.compile(findheader_regex)
        findsetex_regex = r'=+|-+'
        findsetex = re.compile(findsetex_regex)
        new_lines = []
        preline = ''
        for line in lines:
            m = findheaders.match(line)
            s = findsetex.match(line)
            if m:
                new_lines.append(preline)
                if len(m.group('level')) != 2:
                    preline = m.group('header')
                else:
                    preline = line
            elif s and len(line)>2:
                pass
            else:
                new_lines.append(preline)
                preline = line

        new_lines.append(preline)
        return new_lines

class OnlyOneHeaderExtension(markdown.Extension):
    def extendMarkdown(self, md, md_globals):
        md.preprocessors.add("singeheader", OnlyOneHeader(self), "<reference")

def makeExtension(configs = None):
    return OnlyOneHeaderExtension(configs=configs)
