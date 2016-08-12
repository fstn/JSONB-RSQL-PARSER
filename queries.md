select  "invoice.content.invoiceLines.test.testArray"->'coucou'
from invoice , jsonb_array_elements(invoice.content->'invoiceLines') as "invoice.content.invoiceLines",
jsonb_array_elements("invoice.content.invoiceLines"->'test'->'testArray') as "invoice.content.invoiceLines.test.testArray"
where content->'header' @> '{"id":"201607201638331"}' ;


SELECT "invoice.content.invoiceLines.tax.text" FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines')
  as "invoice.content.invoiceLines", jsonb_array_elements("invoice.content.invoiceLines"->'tax'->'text') as "invoice
.content.invoiceLines.tax.text";


SELECT "invoice.content.invoiceLines"->'tax'->'rate' FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines')
  as "invoice.content.invoiceLines";

SELECT "invoice.content.invoiceLines[].tax{}.text"->'value'
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines')
  as "invoice.content.invoiceLines[]", jsonb_array_elements("invoice.content.invoiceLines[]"->'tax'->'text')
  as "invoice.content.invoiceLines[].tax{}.text";



SELECT "invoice.content.invoiceLines.tax.text"->'obj'->'name'
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines')
  as "invoice.content.invoiceLines", jsonb_array_elements("invoice.content.invoiceLines"->'tax'->'text')
  as "invoice.content.invoiceLines.tax.text";

SELECT "invoice.content.header" FROM invoice, jsonb_array_elements(invoice.content->'header') as "invoice.content
.header";

SELECT * FROM invoice, jsonb_array_elements(invoice.content->'header') AS "invoice.content.header[]";

SELECT "invoice.content.invoiceLines[]"->'tax'
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines')
  AS "invoice.content.invoiceLines[]";


SELECT "invoice.content.invoiceLines[].tax{}.text[]"
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines')
  AS "invoice.content.invoiceLines[]", jsonb_array_elements("invoice.content.invoiceLines[]"->'tax'->'text')
  AS "invoice.content.invoiceLines[].tax{}.text[]";


SELECT invoice.content->'header'->'id' FROM invoice WHERE content->'header' @> '{"id":"201607201638331"}';

SELECT invoice.content->'header'->'id'
FROM invoice
WHERE content->'header' @>'{"id":"abdef"}' AND content->'invoiceLines' @> '{"tax":1235}';


SELECT "invoice.content.invoiceLines[]"->'tax'
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines')
  AS "invoice.content.invoiceLines[]" WHERE content->'invoiceLines' @> '{"tax":1235}';


SELECT "INVOICE.content.invoiceLines[]"->'tax' FROM INVOICE,
  jsonb_array_elements(INVOICE.content->'invoiceLines') AS "INVOICE.content.invoiceLines[]"
WHERE "INVOICE.content.invoiceLines[]"->'tax' @> '{"id":1}'


SELECT "INVOICE.content.invoiceLines[]"->'tax'
FROM INVOICE, jsonb_array_elements(INVOICE.content->'invoiceLines') AS "INVOICE.content.invoiceLines[]"
WHERE content->'invoiceLines' @> '[{"tax":1}]'



SELECT "content.invoiceLines[]"->'tax' FROM INVOICE,
jsonb_array_elements(INVOICE.content->'invoiceLines') AS "content.invoiceLines[]", "content.invoiceLines[]"->'tax' AS
obj
WHERE "content.invoiceLines[]"->'tax' @> '{"id":1}';

SELECT "content.invoiceLines[]"->'tax' FROM INVOICE,
  jsonb_array_elements(INVOICE.content->'invoiceLines')
    AS  "content.invoiceLines[]" WHERE "content.invoiceLines[]"->'tax' @> '{"id":1}';


SELECT "content.invoiceLines[]"->'tax'
FROM INVOICE, jsonb_array_elements(INVOICE.content->'invoiceLines')
  AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'tax' @> '{"id":1}';


SELECT "content.invoiceLines[]"->'tax'
FROM INVOICE, jsonb_array_elements(INVOICE.content->'invoiceLines') AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'tax' @> '{"id":12}';


SELECT * FROM INVOICE WHERE "invoice.content.header" @> '{"id":"2016072214582920"}' AND "invoice.content.header.tenant"
                                                                                       @> '{"id":1}';


SELECT invoice.content->'header'->'id' FROM invoice, invoice.content as t(titi) WHERE titi->'header' @>
                                                                                             '{"id":"abdef"}'

SELECT * FROM INVOICE
WHERE content->'header' @> '{"id":"201607201638331"}'
                                     AND content->'header'->'tenant'

SELECT "content.invoiceLines[]"->'tax'
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines') AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'tax' @> '{"id":1}';


SELECT "content.invoiceLines[]"->'tax'
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines') AS "content.invoiceLines[]"
WHERE content->'invoiceLines'->'tax' @> '{"id":1}'

SELECT "content.invoiceLines[]"->'tax'
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines') AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'tax' @> '{"id":1}'

SELECT "content.invoiceLines[]"->'tax'
FROM invoice, jsonb_array_elements(invoice.content->'invoiceLines') AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'tax' @> '{"id":1}'

SELECT "content.invoiceLines[]"
FROM INVOICE, jsonb_array_elements(INVOICE.content->'invoiceLines') AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'obj'->'array'->'obj'->'array'->'array' @> '{"id":1}'


SELECT "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
FROM INVOICE, jsonb_array_elements(INVOICE.content->'invoiceLines') AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[].obj{}.array[]"->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"

SELECT "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
FROM INVOICE, jsonb_array_elements(INVOICE.content->'invoiceLines') AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[].obj{}.array[]"->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
WHERE "content.invoiceLines[]"->'obj'->'array'->'obj'->'array'->'array' @> '[{"id":1}]'


SELECT "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
FROM INVOICE, jsonb_array_elements(INVOICE.content->'invoiceLines') AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[].obj{}.array[]"->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
WHERE "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]" @> '{"id":1}'

SELECT "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
FROM INVOICE, jsonb_array_elements(INVOICE.content->'invoiceLines') AS "content.invoiceLines[]"
  , jsonb_array_elements("content.invoiceLines[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[]"
  , jsonb_array_elements("content.invoiceLines[].obj{}.array[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[]"
  , jsonb_array_elements("content.invoiceLines[].obj{}.array[].obj{}.array[]"->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
WHERE content->'invoiceLines'->'obj'->'array'->'obj'->'array'->'array' @> '{"id":1}'

SELECT * FROM INVOICE ,jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]",
jsonb_array_elements("content.invoiceLines[]"->'array') AS "content.invoiceLines[].array[]"
WHERE "content.invoiceLines[]"->'array' = '201615154'

SELECT "content.invoiceLines[]"->'tax' FROM INVOICE ,
  jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'tax' @> '{"id":1}';

SELECT "content.invoiceLines[]"->'tax' FROM (
  SELECT *
  FROM INVOICE
  WHERE content->'invoiceLines' @> '[{"tax":{"id":1}}]'
  ) as obj,
  jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'tax' @> '{"id":1}';



SELECT "content.invoiceLines[]"->'tax' FROM INVOICE ,
  jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[]"->'obj'->'array')
    AS "content.invoiceLines[].obj{}.array[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[].obj{}.array[]"->'array')
    AS "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
WHERE "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"->'obj' @> '{"id":1}'


SELECT "content.invoiceLines[]"->'tax'
FROM INVOICE ,jsonb_array_elements(content->'invoiceLines')
  AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'obj'->'array')
    AS "content.invoiceLines[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[]"->'obj'->'array')
    AS "content.invoiceLines[].obj{}.array[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[].obj{}.array[]"->'array')
    AS "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
WHERE "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"->'obj' @> '{"id":1}'



SELECT "content.invoiceLines[]"->'array'
FROM ( SELECT * FROM INVOICE WHERE content->'invoiceLines'->'array' = '201615154' ) AS "INVOICE",
  jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'array') AS "content.invoiceLines[].array[]"
WHERE "content.invoiceLines[]"->'array' = '201615154'

SELECT "content.invoiceLines[].array[]"->'obj' FROM (
                                                      SELECT * FROM INVOICE WHERE content->'invoiceLines'->'array'->'obj' = '201615154' ) AS "INVOICE",
  jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'array') AS "content.invoiceLines[].array[]"
WHERE "content.invoiceLines[].array[]"->'obj' = '201615154'


SELECT content->'header'->'tenant'->'id' FROM
  ( SELECT * FROM INVOICE WHERE content->'header'->'tenant' @> '{"id":1}' ) AS "INVOICE"
WHERE content->'header'->'tenant' @> '{"id":1}'


SELECT content->'id' FROM (
                            SELECT * FROM INVOICE WHERE content->'invoiceLines' @> '{"tax":{"id":{"id":9}}}' ) AS "INVOICE",
  jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]"
WHERE "content.invoiceLines[]"->'tax' @> '{"id":9}'



SELECT content->'header'->'tenant'->'id'
FROM ( SELECT * FROM INVOICE WHERE content->'header'->'tenant'->'id' @> '{content"header":{"tenant":{"id":{"id":1}}}}' ) AS "INVOICE"
WHERE content->'header'->'tenant' @> '{"id":1}'

SELECT content->'id' FROM ( SELECT * FROM INVOICE WHERE content->'invoiceLines' @> [{"tax":{"id":9}}] ) AS "INVOICE",jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]" WHERE "content.invoiceLines[]"->'tax' @> '{"id":9}']


SELECT "content.invoiceLines[].array[]"->'obj' FROM ( SELECT * FROM INVOICE WHERE content->'invoiceLines'->'array'->'obj' = '{"content->'invoiceLines'->'array'->'obj'":201615154}' ) AS "INVOICE",jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]",jsonb_array_elements("content.invoiceLines[]"->'array') AS "content.invoiceLines[].array[]" WHERE "content.invoiceLines[].array[]"->'obj' = '201615154'

SELECT content->'invoiceLines'->'tax' FROM (
                                             SELECT * FROM INVOICE WHERE content->'lines' @> '[{"orderNbr":{"orderNbr":"S31992134"}}]' and content->'tenant' @> '[{"id":{"id":1}}]'
                                           ) AS "INVOICE",jsonb_array_elements(content->'lines') AS "content.lines[]",jsonb_array_elements(content->'tenant') AS "content.tenant[]" WHERE "content.lines[]" @> '{"orderNbr":"S31992134"}' and "content.tenant[]" @> '{"id":1}'


SELECT "content.invoiceLines[]"->'tax'
FROM (
       SELECT * FROM INVOICE WHERE content->'invoiceLines' @> '[{"obj":{"array":[{"obj":{"array":[{"array":[{"obj":{"id":{"id":1}}}]}]}}]}}]'
     ) AS "INVOICE",jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[].obj{}.array[]"->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
WHERE "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"->'obj' @> '{"id":1}'

SELECT "content.invoiceLines[]"->'tax'
FROM INVOICE ,jsonb_array_elements(content->'invoiceLines') AS "content.invoiceLines[]",
  jsonb_array_elements("content.invoiceLines[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[]"->'obj'->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[]",
  jsonb_array_elements("content.invoiceLines[].obj{}.array[].obj{}.array[]"->'array') AS "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"
WHERE "content.invoiceLines[].obj{}.array[].obj{}.array[].array[]"->'obj' @> '{"id":1}'

SELECT "content.header[]"->'tenant'
FROM INVOICE ,jsonb_array_elements(content->'header') AS "content.header[]"
