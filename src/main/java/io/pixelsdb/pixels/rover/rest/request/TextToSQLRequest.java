package io.pixelsdb.pixels.rover.rest.request;

import java.util.List;

public class TextToSQLRequest {
    private Schema schema;
    private String text;

    public TextToSQLRequest() {}

    public TextToSQLRequest(Schema schema, String text) {
        this.schema = schema;
        this.text = text;
    }

    public Schema getSchema() { return schema; }

    public void setSchema(Schema schema) { this.schema = schema; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public static class Schema {
        private List<SchemaItem> schema_items;
        private List<List<String>> foreign_keys;

        public Schema() {}

        public Schema(List<SchemaItem> schema_items, List<List<String>> foreign_keys) {
            this.schema_items = schema_items;
            this.foreign_keys = foreign_keys;
        }

        public List<SchemaItem> getSchema_items() { return schema_items; }

        public void setSchema_items(List<SchemaItem> schema_items) { this.schema_items = schema_items; }

        public List<List<String>> getForeign_keys() { return foreign_keys; }

        public void setForeign_keys(List<List<String>> foreign_keys) { this.foreign_keys = foreign_keys; }

        public static class SchemaItem {
            private String table_name;
            private String table_comment;
            private List<String> column_names;
            private List<String> column_types;
            private List<String> column_comments;
            private List<List<String>> column_contents;
            private List<Integer> pk_indicators;

            public SchemaItem() {}

            public SchemaItem(String table_name, String table_comment, List<String> column_names, List<String> column_types, List<String> column_comments, List<List<String>> column_contents, List<Integer> pk_indicators) {
                this.table_name = table_name;
                this.table_comment = table_comment;
                this.column_names = column_names;
                this.column_types = column_types;
                this.column_comments = column_comments;
                this.column_contents = column_contents;
                this.pk_indicators = pk_indicators;
            }

            public String getTable_name() { return table_name; }

            public void setTable_name(String table_name) { this.table_name = table_name; }

            public String getTable_comment() { return table_comment; }

            public void setTable_comment(String table_comment) { this.table_comment = table_comment; }

            public List<String> getColumn_names() { return column_names; }

            public void setColumn_names(List<String> column_names) { this.column_names = column_names; }

            public List<String> getColumn_types() { return column_types; }

            public void setColumn_types(List<String> column_types) { this.column_types = column_types; }

            public List<String> getColumn_comments() { return column_comments; }

            public void setColumn_comments(List<String> column_comments) { this.column_comments = column_comments; }

            public List<List<String>> getColumn_contents() { return column_contents; }

            public void setColumn_contents(List<List<String>> column_contents) { this.column_contents = column_contents; }

            public List<Integer> getPk_indicators() { return pk_indicators; }

            public void setPk_indicators(List<Integer> pk_indicators) { this.pk_indicators = pk_indicators; }
        }
    }
}
