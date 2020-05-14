package com.alibaba.alink.common.utils;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for OutputColsHelper.
 */
public class OutputColsHelperTest {

    private TableSchema tableSchema = new TableSchema(
        new String[]{"f0", "f1", "f2"},
        new TypeInformation[]{
            TypeInformation.of(String.class),
            TypeInformation.of(Long.class),
            TypeInformation.of(Integer.class)
        }
    );
    private String[] reservedColNames = new String[]{"f0"};
    private Row row = Row.of("a", 1L, 1);

    @Test
    public void testResultSchema() {
        TableSchema expectSchema = new TableSchema(
            new String[]{"f0", "f1", "f2", "res"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Long.class),
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            }
        );
        OutputColsHelper helper = new OutputColsHelper(
            tableSchema, "res",
            TypeInformation.of(String.class)
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "res"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(String.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema, "res",
            TypeInformation.of(String.class),
            reservedColNames
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "res1", "res2"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(String.class),
                TypeInformation.of(Integer.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema,
            new String[]{"res1", "res2"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Integer.class)},
            reservedColNames
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "f1", "f2", "res"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Long.class),
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema, "res",
            TypeInformation.of(String.class)
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "f1", "f2"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(Long.class),
                TypeInformation.of(Integer.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema, "f0",
            TypeInformation.of(Integer.class)
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "f1", "f2"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(Long.class),
                TypeInformation.of(String.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema, new String[]{"f0", "f2"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            }
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "res"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Integer.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema, new String[]{"res", "f0"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            },
            reservedColNames
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "f1", "res"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Long.class),
                TypeInformation.of(Integer.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema, new String[]{"res"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)},
            new String[]{"f1", "f0"}
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);
    }

    @Test
    public void testResultRow() {
        OutputColsHelper helper = new OutputColsHelper(
            tableSchema, "res", TypeInformation.of(String.class)
        );
        Row expectRow = Row.of("a", 1L, 1, "b");
        Assert.assertEquals(helper.getResultRow(row, Row.of("b")), expectRow);

        helper = new OutputColsHelper(
            tableSchema, new String[]{"res1", "res2"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Integer.class)
            }
        );
        expectRow = Row.of("a", 1L, 1, "b", 2);
        Assert.assertEquals(helper.getResultRow(row, Row.of("b", 2)), expectRow);

        helper = new OutputColsHelper(
            tableSchema, new String[]{"res", "f0"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            },
            reservedColNames
        );
        expectRow = Row.of("b", 2);
        Assert.assertEquals(helper.getResultRow(row, Row.of(2, "b")), expectRow);
    }

    @Test
    public void testExceptionCase() {
        TableSchema expectSchema = new TableSchema(
            new String[]{"f0", "res"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Integer.class)
            }
        );
        OutputColsHelper helper = new OutputColsHelper(
            tableSchema, new String[]{"res", "f0"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            },
            new String[]{"res", "res2"}
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "f1", "res"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Long.class),
                TypeInformation.of(Integer.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema, new String[]{"res", "f0"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            },
            new String[]{"f1", "res"}
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);

        expectSchema = new TableSchema(
            new String[]{"f0", "f1", "f2"},
            new TypeInformation[]{
                TypeInformation.of(String.class),
                TypeInformation.of(Integer.class),
                TypeInformation.of(Double.class)
            }
        );
        helper = new OutputColsHelper(
            tableSchema, new String[]{"f1", "f0", "f2"},
            new TypeInformation[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class),
                TypeInformation.of(Double.class)
            },
            new String[]{"f1", "res"}
        );
        Assert.assertEquals(helper.getResultSchema(), expectSchema);
    }
}
