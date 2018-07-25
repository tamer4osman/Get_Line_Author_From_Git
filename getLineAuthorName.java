public String getLineAuthorName(Path path, int lineNo) {
        String author = null;
        String fullPath;
        try {
            if (path.isAbsolute()) fullPath = path.toString();
            else fullPath= m_workingRoot.relativize(path).toString();
            Result r = Execute.processAndValidate(Arrays.asList("git", "blame"
                        , "-p"
                        , "-L" + lineNo + "," + lineNo
                        , "--", fullPath)
                    , m_workingRoot
                    , false
                    , Charset.defaultCharset());
            logger.debug(r.GetOutputMessage());

            author = r.GetOutputLines().stream().filter(line -> line.startsWith("committer-mail")).findFirst().get();
            author = author.replaceAll(".*[<](.*)[>].*", "$1").toLowerCase();
        } catch (ExecuteFailedException e) {
            logger.error("Blame failed with: " + e.toString());
        }
        return author;
