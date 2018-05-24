package com.example.daquexian.flexiblerichtextview.MyFlexibleRichTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 31446 on 2018/5/21.
 */

public class Tokenizer {
    //文字下划线
    private static List<String> underlineStartLabels = new ArrayList<>();
    private static List<String> underlineEndLabels = new ArrayList<>();

    //  文字加点
    private static List<String> dotStartLabels = new ArrayList<>();
    private static List<String> dotEndLabels = new ArrayList<>();

    //  latex格式符号
    private static List<String> latexStartLabels = new ArrayList<>();
    private static List<String> latexEndLabels = new ArrayList<>();

    //  斜体字
    private static List<String> italicStartLabels = new ArrayList<>();
    private static List<String> italicEndLabels = new ArrayList<>();

    //  加粗字体
    private static List<String> boldStartLabels = new ArrayList<>();
    private static List<String> boldEndLabels = new ArrayList<>();


    private static List<String> imgStartLabels = new ArrayList<>();
    private static List<String> imgEndLabels = new ArrayList<>();
    private static List<String> curtainStartLabels = new ArrayList<>();
    private static List<String> curtainEndLabels = new ArrayList<>();


    private static List<String> imageLabels = new ArrayList<>();


    static {
        initLabels();
    }

    private static void initLabels() {
        setUnderlineStartLabels("[u]");
        setUnderlineEndLabels("[/u]");

        setDotStartLabels("[dot]");
        setDotEndLabels("[/dot]");
//        setUnderlineStartLabels("[url=\\s]");
//        setUnderlineStartLabels("[url=\\s]");
//        setUnderlineStartLabels("[url=\\s]");
        setLatexStartLabels("[l]");
        setLatexEndLabels("[/l]");

        setItalicStartLabels("[i]");
        setItalicEndLabels("[/i]");

        setBoldStartLabels("[b]");
        setBoldEndLabels("[/b]");

        setImgStartLabels("[img=\\s]");
        setImgEndLabels("[/img]");

    }

    private static String formatLabel(String label) {
        //"(?i)"匹配时不区分大小写, \\[
        return "(?i)" + label.replaceAll("\\[", "\\\\[").replaceAll("\\(", "\\\\(");
    }

    public static int setUnderlineStartLabels(String... labels) {
        int ret = labels.length;
        underlineStartLabels = new ArrayList<>();
        for (String label : labels) {
            underlineStartLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setUnderlineEndLabels(String... labels) {
        int ret = labels.length;
        underlineEndLabels = new ArrayList<>();
        for (String label : labels) {
            underlineEndLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setDotStartLabels(String... labels) {
        int ret = labels.length;
        dotStartLabels = new ArrayList<>();
        for (String label : labels) {
            dotStartLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setDotEndLabels(String... labels) {
        int ret = labels.length;
        dotEndLabels = new ArrayList<>();
        for (String label : labels) {
            dotEndLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setLatexStartLabels(String... labels) {
        int ret = labels.length;
        latexStartLabels = new ArrayList<>();
        for (String label : labels) {
            latexStartLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setLatexEndLabels(String... labels) {
        int ret = labels.length;
        latexEndLabels = new ArrayList<>();
        for (String label : labels) {
            latexEndLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setItalicStartLabels(String... labels) {
        int ret = labels.length;
        italicStartLabels = new ArrayList<>();
        for (String label : labels) {
            italicStartLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setItalicEndLabels(String... labels) {
        int ret = labels.length;
        italicEndLabels = new ArrayList<>();
        for (String label : labels) {
            italicEndLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setBoldStartLabels(String... labels) {
        int ret = labels.length;
        boldStartLabels = new ArrayList<>();
        for (String label : labels) {
            boldStartLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setBoldEndLabels(String... labels) {
        int ret = labels.length;
        boldEndLabels = new ArrayList<>();
        for (String label : labels) {
            boldEndLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    public static int setImgStartLabels(String... labels) {
        int ret = labels.length;

        imgStartLabels = new ArrayList<>();
        for (String label : labels) {
            if (label.contains("\\s")) {
                imgStartLabels.add(formatLabel(label)
                        .replaceAll("\\\\s", "(.+?)"));
                ret--;
            }
        }
        return ret;
    }

    public static int setImgEndLabels(String... labels) {
        int ret = labels.length;
        imgEndLabels = new ArrayList<>();
        for (String label : labels) {
            imgEndLabels.add(formatLabel(label));
            ret--;
        }
        return ret;
    }

    static abstract class TOKEN{
        int position;
        int length;
        CharSequence value;

        public TOKEN(int position, int length, CharSequence value) {
            this.position = position;
            this.length = length;
            this.value = value;
        }

        public static String getString(List<TOKEN> tokens) {
            StringBuilder builder = new StringBuilder();
            for (Tokenizer.TOKEN token : tokens) {
                builder.append(token.value);
            }
            return builder.toString();
        }

    }

    static class UNDERLINE_START extends TOKEN {
        UNDERLINE_START(int position, String value) {
            super(position, value.length(), value);
        }
    }
    static class UNDERLINE_END extends TOKEN {
        UNDERLINE_END(int position, String value) {
            super(position, value.length(), value);
        }
    }

    static class BOLD_START extends TOKEN {
        BOLD_START(int position, String value) {
            super(position, value.length(), value);
        }
    }

    static class BOLD_END extends TOKEN {
        BOLD_END(int position, String value) {
            super(position, value.length(), value);
        }
    }

    static class ITALIC_START extends TOKEN {
        ITALIC_START(int position, String value) {
            super(position, value.length(), value);
        }
    }

    static class ITALIC_END extends TOKEN {
        ITALIC_END(int position, String value) {
            super(position, value.length(), value);
        }
    }

    static class LATEX_START extends TOKEN {
        LATEX_START(int position, String value) {
            super(position, value.length(), value);
        }
    }

    static class LATEX_END extends TOKEN {
        LATEX_END(int position, String value) {
            super(position, value.length(), value);
        }
    }

    static class END extends TOKEN {
        END(int position) {
            super(position, 0, "");
        }
    }

    static class PLAIN extends TOKEN {
        PLAIN(int position, CharSequence value) {
            super(position, value.length(), value);
        }
    }

    static class IMG_START extends TOKEN {
        String url;
        IMG_START(int position, String url, String value) {
            super(position, value.length(), value);
            this.url = url;
        }
    }
    static class IMG_END extends TOKEN {
        IMG_END(int position, String value) {
            super(position, value.length(), value);
        }
    }

    static class FORMULA extends TOKEN {//格式化的字符串，代表Latex
        String content;
        int contentStart;
        FORMULA(int position, String content, int contentStart, String value) {
            /*
             * remove all newline character to avoid the ImageSpan shows multiple times when
             * formula content stretches over multiple lines.
             */
            super(position, value.length(), value.replaceAll("[\n\r]", ""));
            this.content = content.replaceAll("[\n\r]", "");
            this.contentStart = contentStart;
        }
    }

    public static void main(String[] args) {
        String text = "[u]aaaa[/u][b]aaa[/b][img=http://www.baidu.com][/img]";
        Tokenizer.tokenizer(text);
    }

    private static final Pattern FORMULA_REG1 = Pattern.compile("(?i)\\$\\$?((.|\\n)+?)\\$\\$?");
    private static final Pattern FORMULA_REG2 = Pattern.compile("(?i)\\\\[(\\[]((.|\\n)*?)\\\\[\\])]");
    private static final Pattern FORMULA_REG3 = Pattern.compile("(?i)\\[tex]((.|\\n)*?)\\[/tex]");
    private static final Pattern FORMULA_REG4 = Pattern.compile("(?i)\\\\begin\\{.*?\\}(.|\\n)*?\\\\end\\{.*?\\}");
    // private static final Pattern FORMULA_REG5 = Pattern.compile("(?i)\\$\\$(.+?)\\$\\$");
    private static final Pattern[] PATTERNS = {FORMULA_REG1, FORMULA_REG2, FORMULA_REG3, FORMULA_REG4};

    private static final Pattern IMG_REG = Pattern.compile("(?i)\\[img(=\\d+)?](.*?)\\[/img]");

    private static final Pattern TABLE_REG = Pattern.compile("(?:\\n|^)( *\\|.+\\| *\\n)??( *\\|(?: *:?----*:? *\\|)+ *\\n)((?: *\\|.+\\| *(?:\\n|$))+)");

    public static List<TOKEN> tokenizer(CharSequence text) {

        List<TOKEN> tokenList = new ArrayList<>();

        Pattern pattern;
        Matcher matcher;
        int start;

        for (String underlineStartLabel : underlineStartLabels) {
            pattern = Pattern.compile(underlineStartLabel);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                tokenList.add(new UNDERLINE_START(matcher.start(), matcher.group()));
            }
        }

        for (String underlineEndLabel : underlineEndLabels) {
            pattern = Pattern.compile(underlineEndLabel);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                tokenList.add(new UNDERLINE_END(matcher.start(), matcher.group()));
            }
        }

        for (String boldStartLabel : boldStartLabels) {
            pattern = Pattern.compile(boldStartLabel);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                tokenList.add(new BOLD_START(matcher.start(), matcher.group()));
            }
        }

        for (String boldEndLabel : boldEndLabels) {
            pattern = Pattern.compile(boldEndLabel);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                tokenList.add(new BOLD_END(matcher.start(), matcher.group()));
            }
        }

        for (String italicStartLabel : italicStartLabels) {
            pattern = Pattern.compile(italicStartLabel);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                tokenList.add(new ITALIC_START(matcher.start(), matcher.group()));
            }
        }

        for (String italicEndLabel : italicEndLabels) {
            pattern = Pattern.compile(italicEndLabel);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                tokenList.add(new ITALIC_END(matcher.start(), matcher.group()));
            }
        }

        for (String latexStartLabel : latexStartLabels) {
            pattern = Pattern.compile(latexStartLabel);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                tokenList.add(new LATEX_START(matcher.start(), matcher.group()));
            }
        }

        for (String latexEndLabel : latexEndLabels) {
            pattern = Pattern.compile(latexEndLabel);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                tokenList.add(new LATEX_END(matcher.start(), matcher.group()));
            }
        }


        for (String imageLabel : imgStartLabels) {
            pattern = Pattern.compile(imageLabel);
            matcher = pattern.matcher(text);

            while (matcher.find()) {
                String url = matcher.group(1).toLowerCase();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                tokenList.add(new IMG_START(matcher.start(), url, matcher.group()));
            }
        }

        for (String imgEndLabel : imgEndLabels) {
            pattern = Pattern.compile(imgEndLabel);
            matcher = pattern.matcher(text);

            while (matcher.find()) {
                tokenList.add(new IMG_END(matcher.start(),matcher.group()));
            }
        }

        //      获取文字信息
        start = 0;
        for (int i = 0; i < tokenList.size(); i++) {
            TOKEN token = tokenList.get(i);
            if (token.position > start) {
                tokenList.add(i, new PLAIN(start, text.subSequence(start, token.position)));
                System.out.println(text.subSequence(start, token.position));
                i++;
            }
            start = token.position + token.length;
        }
//        for (String urlStartLabel : urlStartLabels) {
//            pattern = Pattern.compile(urlStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                String url = matcher.group(1).toLowerCase();
//                if (!url.startsWith("http://") && !url.startsWith("https://")) {
//                    url = "http://" + url;
//                }
//                tokenList.add(new URL_START(matcher.start(), url, matcher.group()));
//
//            }
//
//        }
//
//        for (String urlEndLabel : urlEndLabels) {
//            pattern = Pattern.compile(urlEndLabel);
//            matcher = pattern.matcher(text);
//
//
//            while (matcher.find()) {
//                tokenList.add(new URL_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String centerStartLabel : centerStartLabels) {
//            pattern = Pattern.compile(centerStartLabel);
//            matcher = pattern.matcher(text);
//
//
//            while (matcher.find()) {
//                tokenList.add(new CENTER_START(matcher.start(), matcher.group()));
//
//            }
//        }
//
//        for (String centerEndLabel : centerEndLabels) {
//            pattern = Pattern.compile(centerEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new CENTER_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String curtainStartLabel : curtainStartLabels) {
//            pattern = Pattern.compile(curtainStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new CURTAIN_START(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String curtainEndLabel : curtainEndLabels) {
//            pattern = Pattern.compile(curtainEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new CURTAIN_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String attachmentLabel : attachmentLabels) {
//            pattern = Pattern.compile(attachmentLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                String id = matcher.group(1);
//                if (attachmentList != null) {
//                    for (Attachment attachment : attachmentList) {
//                        if (attachment.getAttachmentId().equals(id)) {
//                            tokenList.add(new ATTACHMENT(matcher.start(), attachment, matcher.group()));
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        for (String colorEndLabel : colorEndLabels) {
//            pattern = Pattern.compile(colorEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new COLOR_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String italicStartLabel : italicStartLabels) {
//            pattern = Pattern.compile(italicStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new ITALIC_START(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String italicEndLabel : italicEndLabels) {
//            pattern = Pattern.compile(italicEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new ITALIC_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String boldStartLabel : boldStartLabels) {
//            pattern = Pattern.compile(boldStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new BOLD_START(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String boldEndLabel : boldEndLabels) {
//            pattern = Pattern.compile(boldEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new BOLD_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String deleteStartLabel : deleteStartLabels) {
//            pattern = Pattern.compile(deleteStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new DELETE_START(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String deleteEndLabel : deleteEndLabels) {
//            pattern = Pattern.compile(deleteEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new DELETE_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String underlineStartLabel : underlineStartLabels) {
//            pattern = Pattern.compile(underlineStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new UNDERLINE_START(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String underlineEndLabel : underlineEndLabels) {
//            pattern = Pattern.compile(underlineEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new UNDERLINE_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String titleStartLabel : titleStartLabels) {
//            pattern = Pattern.compile(titleStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new TITLE_START(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String titleEndLabel : titleEndLabels) {
//            pattern = Pattern.compile(titleEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new TITLE_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String codeStartLabel : codeStartLabels) {
//            pattern = Pattern.compile(codeStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new CODE_START(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (String codeEndLabel : codeEndLabels) {
//            pattern = Pattern.compile(codeEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new CODE_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        for (int i = 0; i < quoteStartLabels.size(); i++) {
//            String quoteStartLabel = quoteStartLabels.get(i);
//            QuotePos quotePos = quotePosList.get(i);
//            pattern = Pattern.compile(quoteStartLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                if (quotePos.postIdPos == -1 && quotePos.memberPos == -1) {
//                    tokenList.add(new QUOTE_START(matcher.start(), matcher.group(), "", ""));
//                } else if (quotePos.postIdPos == -1) {
//                    tokenList.add(new QUOTE_START(matcher.start(), matcher.group(), matcher.group(1), ""));
//                } else if (quotePos.memberPos == -1) {
//                    tokenList.add(new QUOTE_START(matcher.start(), matcher.group(), "", matcher.group(1)));
//                } else {
//                    tokenList.add(new QUOTE_START(matcher.start(), matcher.group(), matcher.group(quotePos.memberPos), matcher.group(quotePos.postIdPos)));
//                }
//            }
//        }
//
//        for (String quoteEndLabel : quoteEndLabels) {
//            pattern = Pattern.compile(quoteEndLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                tokenList.add(new QUOTE_END(matcher.start(), matcher.group()));
//            }
//        }
//
//        String str = text.toString();
//        for (int i = 0; i < iconStrs.size(); i++) {
//            int from = 0;
//            String iconStr = iconStrs.get(i);
//            while ((from = str.indexOf(iconStr, from)) >= 0) {
//
//                /**
//                 * only show icons when iconStr is surrounded by spaces
//                 */
//                if (iconStr.equals("/^^")) Log.d(TAG, "parse: " + str.trim().length() + ", " + iconStr.length() + ", " + (from + iconStr.length()) + ", " + str.length());
//                if (str.trim().length() == iconStr.length() ||
//                        ((from == 0 || ' ' == str.charAt(from - 1)) && (from + iconStr.length() == str.length() || ' ' == str.charAt(from + iconStr.length()) || '\n' == str.charAt(from + iconStr.length())))) {
//                    tokenList.add(new ICON(from, iconStr, icons.get(i)));
//                }
//                from += iconStr.length();
//            }
//        }
//
//        for (int i = 0; i < imageLabels.size(); i++) {
//            String imageLabel = imageLabels.get(i);
//            ImgPos imgPos = imgPosList.get(i);
//            pattern = Pattern.compile(imageLabel);
//            matcher = pattern.matcher(text);
//
//            while (matcher.find()) {
//                if (imgPos.heightPos == -1 && imgPos.widthPos == -1) {
//                    tokenList.add(new IMAGE(matcher.start(), matcher.group(1), matcher.group()));
//                } else if (imgPos.heightPos == -1) {
//                    tokenList.add(new IMAGE(matcher.start(), matcher.group(imgPos.urlPos), matcher.group(), Integer.valueOf(matcher.group(imgPos.widthPos)), -1));
//                } else if (imgPos.widthPos == -1) {
//                    tokenList.add(new IMAGE(matcher.start(), matcher.group(imgPos.urlPos), matcher.group(), -1, Integer.valueOf(matcher.group(imgPos.heightPos))));
//                } else {
//                    tokenList.add(new IMAGE(matcher.start(), matcher.group(imgPos.urlPos), matcher.group(), Integer.valueOf(matcher.group(imgPos.widthPos)), Integer.valueOf(matcher.group(imgPos.heightPos))));
//                }
//            }
//        }
//
//        pattern = TABLE_REG;
//        matcher = pattern.matcher(text);
//
//        while (matcher.find()) {
//            tokenList.add(new TABLE(matcher.start(), matcher.group()));
//        }
//
//
//        final int[] indexInRegex = {1, 1, 1, 0, 1};
//        Matcher[] matchers = new Matcher[PATTERNS.length];
//        for (int i = 0; i < PATTERNS.length; i++) {
//            matchers[i] = PATTERNS[i].matcher(text);
//        }
//
//        for (int i = 0; i < matchers.length; i++) {
//            matcher = matchers[i];
//            int index = indexInRegex[i];
//
//            String content, value;
//            int contentStart;
//
//            while (matcher.find()) {
//                start = matcher.start();
//                content = matcher.group(index);
//                value = matcher.group();
//
//                contentStart = matcher.start(index);
//
//                tokenList.add(new FORMULA(start, content, contentStart - start, value));
//            }
//        }
//
//        Collections.sort(tokenList);
//
//        for (int i = 0; i < tokenList.size(); i++) {
//            TOKEN token = tokenList.get(i);
//
//            if (token instanceof TABLE) {
//                for (int j = 0; j < tokenList.size(); j++) {
//                    TOKEN token1 = tokenList.get(j);
//
//                    if (token1.position >= token.position + token.length) {
//                        break;
//                    }
//
//                    if (token1.position > token.position) {
//                        tokenList.remove(j);
//                        j--;
//                    }
//                }
//            }
//        }
//

//        removeOverlappingTokens(tokenList);

        start = 0;
        for (int i = 0; i < tokenList.size(); i++) {
            TOKEN token = tokenList.get(i);
            if (token.position > start) {
                tokenList.add(i, new PLAIN(start, text.subSequence(start, token.position)));
                i++;
            }
            start = token.position + token.length;
        }

        final int[] indexInRegex = {1, 1, 1, 0, 1};
        Matcher[] matchers = new Matcher[PATTERNS.length];
        for (int i = 0; i < PATTERNS.length; i++) {
            matchers[i] = PATTERNS[i].matcher(text);
        }

        for (int i = 0; i < matchers.length; i++) {
            matcher = matchers[i];
            int index = indexInRegex[i];

            String content, value;
            int contentStart;

            while (matcher.find()) {
                start = matcher.start();
                content = matcher.group(index);
                value = matcher.group();

                contentStart = matcher.start(index);
                System.out.println("格式："+new FORMULA(start, content, contentStart - start, value).content);
                tokenList.add(new FORMULA(start, content, contentStart - start, value));

            }
        }
        tokenList.add(new END(text.length()));
        return tokenList;
    }
}
