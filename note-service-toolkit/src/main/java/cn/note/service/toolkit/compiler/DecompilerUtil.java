package cn.note.service.toolkit.compiler;

import cn.hutool.core.util.StrUtil;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns.LineNumberMapping;

import java.util.*;
import java.util.Map.Entry;

/**
 * 反编译工具
 */
public class DecompilerUtil {

    public static String decompile(String classFilePath) {
        return decompile(classFilePath, null, false);
    }

    public static String decompile(String classFilePath, String methodName) {
        return decompile(classFilePath, methodName, false);
    }

    private static String decompileWithMappings(String classFilePath,
                                                String methodName, boolean hideUnicode) {
        final StringBuilder sb = new StringBuilder(8192);

        final NavigableMap<Integer, Integer> lineMapping = new TreeMap<Integer, Integer>();

        OutputSinkFactory mySink = new OutputSinkFactory() {
            @Override
            public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                return Arrays.asList(SinkClass.STRING, SinkClass.DECOMPILED, SinkClass.DECOMPILED_MULTIVER,
                        SinkClass.EXCEPTION_MESSAGE, SinkClass.LINE_NUMBER_MAPPING);
            }

            @Override
            public <T> Sink<T> getSink(final SinkType sinkType, final SinkClass sinkClass) {
                return new Sink<T>() {
                    @Override
                    public void write(T sinkable) {
                        // skip message like: Analysing type demo.MathGame
                        if (sinkType == SinkType.PROGRESS) {
                            return;
                        }
                        if (sinkType == SinkType.LINENUMBER) {
                            LineNumberMapping mapping = (LineNumberMapping) sinkable;
                            NavigableMap<Integer, Integer> classFileMappings = mapping.getClassFileMappings();
                            NavigableMap<Integer, Integer> mappings = mapping.getMappings();
                            if (classFileMappings != null && mappings != null) {
                                for (Entry<Integer, Integer> entry : mappings.entrySet()) {
                                    Integer srcLineNumber = classFileMappings.get(entry.getKey());
                                    lineMapping.put(entry.getValue(), srcLineNumber);
                                }
                            }
                            return;
                        }
                        sb.append(sinkable);
                    }
                };
            }
        };

        HashMap<String, String> options = new HashMap<String, String>();
        /**
         * @see org.benf.cfr.reader.util.MiscConstants.Version.getVersion() Currently,
         *      the cfr version is wrong. so disable show cfr version.
         */
        options.put("showversion", "false");
        options.put("hideutf", String.valueOf(hideUnicode));
        options.put("trackbytecodeloc", "true");
        if (!StrUtil.isBlank(methodName)) {
            options.put("methodname", methodName);
        }

        CfrDriver driver = new CfrDriver.Builder().withOptions(options).withOutputSink(mySink).build();
        List<String> toAnalyse = new ArrayList<String>();
        toAnalyse.add(classFilePath);
        driver.analyse(toAnalyse);
        return sb.toString();
    }

    public static String decompile(String classFilePath, String methodName, boolean hideUnicode) {
        return decompileWithMappings(classFilePath, methodName, hideUnicode);
    }


}