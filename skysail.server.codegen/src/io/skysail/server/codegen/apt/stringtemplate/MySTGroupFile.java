package io.skysail.server.codegen.apt.stringtemplate;

import java.net.*;

import org.stringtemplate.v4.*;
import org.stringtemplate.v4.compiler.*;
import org.stringtemplate.v4.misc.ErrorType;

import st4hidden.org.antlr.runtime.*;

public class MySTGroupFile extends STGroupFile {

    protected char[] data;
    protected int n;

    static {
        verbose = true;
    }

    public MySTGroupFile(String fileName, char delimiterStartChar, char delimiterStopChar) {
        super(fileName, delimiterStartChar, delimiterStopChar);
    }

    public ST getInstanceOf(String name) {
        if (name == null)
            return null;
        if (verbose)
            System.out.println(getName() + ".getInstanceOf(" + name + ")");
        if (name.charAt(0) != '/')
            name = "/" + name;
        CompiledST c = lookupTemplate(name);
        if (c != null) {
            ST st = createStringTemplate(c);
            st.getAttributes().entrySet().stream().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

            return st;
        }
        return null;
    }

    public void loadGroupFile(String prefix, String fileName) {
        if (verbose) {
            System.out.println("************************");
            System.out.println(this.getClass().getSimpleName() + ".loadGroupFile(group-file-prefix=" + prefix
                    + ", fileName=" + fileName + ")");
            System.out.println("Encoding: " + encoding);
        }
        GroupParser parser;
        try {
            URL f = new URL(fileName);
            URLConnection openConnection = f.openConnection();
            openConnection.setUseCaches(false);
            ANTLRInputStream fs = new ANTLRInputStream(openConnection.getInputStream(), encoding);
            GroupLexer lexer = new GroupLexer(fs);
            fs.name = fileName;
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            parser = new GroupParser(tokens);
            parser.group(this, prefix);
        } catch (Exception e) {
            errMgr.IOError(null, ErrorType.CANT_LOAD_GROUP_FILE, e, fileName);
        }
    }


}
