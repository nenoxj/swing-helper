package cn.note.swing.view.note.editor.core.richtext.handler.search;

import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.search.Searchable;

import javax.swing.text.BadLocationException;
import javax.swing.text.Segment;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 完成搜索功能
 * <p>
 * from JXEditorPane
 * @author: jee
 */
@Slf4j
public class SearchHandler implements Searchable {
    /**
     * 编辑器
     */
    private ExtendedEditor htmlEditor;

    public SearchHandler(ExtendedEditor extendedEditor) {
        htmlEditor = extendedEditor;
    }

    private int lastFoundIndex = -1;
    private MatchResult lastMatchResult;
    private String lastRegEx;

    @Override
    public int search(String searchString) {
        return this.search(searchString, -1);
    }

    @Override
    public int search(String searchString, int columnIndex) {
        return this.search(searchString, columnIndex, false);
    }

    @Override
    public int search(String searchString, int columnIndex, boolean backward) {
        Pattern pattern = null;
        if (!this.isEmpty(searchString)) {
            pattern = Pattern.compile(searchString, 0);
        }

        return this.search(pattern, columnIndex, backward);
    }

    private boolean isEmpty(String searchString) {
        return searchString == null || searchString.length() == 0;
    }

    @Override
    public int search(Pattern pattern) {
        return this.search(pattern, -1);
    }

    @Override
    public int search(Pattern pattern, int startIndex) {
        return this.search(pattern, startIndex, false);
    }

    @Override
    public int search(Pattern pattern, int startIndex, boolean backwards) {
        if (pattern == null || htmlEditor.getDocument().getLength() == 0 || startIndex > -1 && htmlEditor.getDocument().getLength() < startIndex) {
            this.updateStateAfterNotFound();
            return -1;
        } else {
            int start = startIndex;
            if (this.maybeExtendedMatch(startIndex)) {
                if (this.foundExtendedMatch(pattern, startIndex)) {
                    return this.lastFoundIndex;
                }

                start = startIndex + 1;
            }

            int length;
            if (backwards) {
                start = 0;
                if (startIndex < 0) {
                    length = htmlEditor.getDocument().getLength() - 1;
                } else {
                    length = -1 + startIndex;
                }
            } else {
                if (start < 0) {
                    start = 0;
                }

                length = htmlEditor.getDocument().getLength() - start;
            }

            Segment segment = new Segment();

            try {
                htmlEditor.getDocument().getText(start, length, segment);
            } catch (BadLocationException var9) {
                log.error("this should not happen (calculated the valid start/length) :{} ", var9);
            }

            Matcher matcher = pattern.matcher(segment.toString());
            MatchResult currentResult = this.getMatchResult(matcher, !backwards);
            if (currentResult != null) {
                this.updateStateAfterFound(currentResult, start);
            } else {
                this.updateStateAfterNotFound();
            }

            return this.lastFoundIndex;
        }
    }

    private boolean foundExtendedMatch(Pattern pattern, int start) {
        if (pattern.pattern().equals(this.lastRegEx)) {
            return false;
        } else {
            int length = htmlEditor.getDocument().getLength() - start;
            Segment segment = new Segment();

            try {
                htmlEditor.getDocument().getText(start, length, segment);
            } catch (BadLocationException var7) {
                log.error("this should not happen (calculated the valid start/length) :{}", var7);
            }

            Matcher matcher = pattern.matcher(segment.toString());
            MatchResult currentResult = this.getMatchResult(matcher, true);
            if (currentResult != null && currentResult.start() == 0 && !this.lastMatchResult.group().equals(currentResult.group())) {
                this.updateStateAfterFound(currentResult, start);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean maybeExtendedMatch(int startIndex) {
        return startIndex >= 0 && startIndex == this.lastFoundIndex;
    }

    private int updateStateAfterFound(MatchResult currentResult, int offset) {
        int end = currentResult.end() + offset;
        int found = currentResult.start() + offset;
        htmlEditor.select(found, end);
        htmlEditor.getCaret().setSelectionVisible(true);
        this.lastFoundIndex = found;
        this.lastMatchResult = currentResult;
        this.lastRegEx = ((Matcher) this.lastMatchResult).pattern().pattern();
        return found;
    }

    private MatchResult getMatchResult(Matcher matcher, boolean useFirst) {
        MatchResult currentResult = null;

        while (matcher.find()) {
            currentResult = matcher.toMatchResult();
            if (useFirst) {
                break;
            }
        }

        return currentResult;
    }

    private void updateStateAfterNotFound() {
        this.lastFoundIndex = -1;
        this.lastMatchResult = null;
        this.lastRegEx = null;
        htmlEditor.setCaretPosition(htmlEditor.getSelectionEnd());
    }
}