package it.unibo.coordination.linda.string;

import it.unibo.coordination.linda.core.Tuple;

class RegexTemplateImpl implements RegexTemplate {

    private final com.google.code.regexp.Pattern pattern;

    public RegexTemplateImpl(String pattern) {
        this.pattern = com.google.code.regexp.Pattern.compile(pattern);
    }

    public RegexTemplateImpl(com.google.code.regexp.Pattern pattern) {
        this.pattern = pattern;
    }

    public RegexTemplateImpl(java.util.regex.Pattern pattern) {
        this.pattern = com.google.code.regexp.Pattern.compile(pattern.pattern());
    }

    @Override
    public java.util.regex.Pattern getTemplate() {
        return pattern.pattern();
    }

    @Override
    public RegularMatch matchWith(Tuple tuple) {
        if (tuple instanceof StringTuple) {
            return new RegularMatchImpl(this, pattern.matcher(((StringTuple) tuple).getTuple()), tuple);
        }

        return RegularMatch.failed(this);
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "/" + pattern + "/";
    }
}
