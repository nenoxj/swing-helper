package cn.note.service.toolkit.formatter;


import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SqlFormatter implements CodeFormatter {

    private static final SQLParserFeature[] FORMAT_DEFAULT_FEATURES;

    private String dbType;

    static {
        FORMAT_DEFAULT_FEATURES = new SQLParserFeature[]{SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup};
    }

    public SqlFormatter(SqlType sqlType) {
        this.dbType = sqlType.getType();
    }

    @Override
    public String format(String originCode) throws FormatterException {
        SQLStatementParser parser = null;
        try {
            parser = SQLParserUtils.createSQLStatementParser(originCode, dbType, FORMAT_DEFAULT_FEATURES);
            List<SQLStatement> statementList = parser.parseStatementList();
            return SQLUtils.toSQLString(statementList, dbType, null, null);
        } catch (ClassCastException var6) {
            log.warn("format error, dbType : " + dbType, var6);
            return originCode;
        } catch (ParserException var7) {
            Lexer lexer = parser.getLexer();
            log.error("sql format error!", var7.getMessage());
            parseError(lexer);
        }
        return null;
    }

    /**
     * 从errorLexer中提取异常信息
     *
     * @param errorLexer 包含格式化异常信息
     * @throws FormatterException
     */
    private void parseError(Lexer errorLexer) throws FormatterException {
        int pos = errorLexer.pos();
        Token token = errorLexer.token();
        int posLine = errorLexer.getPosLine();
        int posColumn = errorLexer.getPosColumn();
        String error = "SQL ERROR: pos {}, line {}, column {}, token {} \n";
        error = StrUtil.format(error, pos, posLine, posColumn, token);
        throw new FormatterException(error, posLine, posColumn);
    }

}
