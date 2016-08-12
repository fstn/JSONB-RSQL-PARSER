# JSONB-RSQL-PARSER

#### RSQL to PostgreSQL JSONB DB with Select clause
```Java       
    // RSQL Query
    final String rsqlQuery = "content.invoiceLines[].tax{}.id@==9";
    final String rsqlQuery = "content.invoiceLines[].tax{}.code@=='\"D10\"'";
    // Select clause
    List<JSONBSelect> selectClause = Collections.singletonList( new JSONBSelect("content.invoiceLines[].tax{}"));
    // Visitor declaration
    final RSQLVisitor<JSONBQuery, EntityManager> visitor = new JSONBQueryVisitor(Invoice.class, selectClause);
    // Parsing
    final Node rootNode = new RSQLParser().parse(rsqlQuery);
    // Getting JSONB query
    final JSONBQuery jsonbQuery = rootNode.accept(visitor, entityManager);
    // Getting query
    Query query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
```

#### RSQL to PostgreSQL JSONB DB with Select clause Faster Way
```Java       
    // RSQL Query
    final String rsqlQuery = "content.invoiceLines[].tax{}.id@==9";
    final String rsqlQuery = "content.invoiceLines[].tax{}.code@=='\"D10\"'";
    // Select clause
    List<JSONBSelect> selectClause = Collections.singletonList( new JSONBSelect("content.invoiceLines[].tax{}"));
    // Visitor declaration
    final RSQLVisitor<JSONBQuery, EntityManager> visitor = new JSONBQueryVisitor(Invoice.class, selectClause);
    // Parsing
    final Node rootNode = new RSQLParser().parse(rsqlQuery);
    // Getting JSONB query
    final JSONBQuery jsonbQuery = rootNode.accept(visitor, entityManager);
    // Enable optimize query mode (use sub select)
    jsonbQuery.setMode(JSONBQueryMode.FAST);
    // Getting query
    Query query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
```
