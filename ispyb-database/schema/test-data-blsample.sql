-- Minimal fixture data for BLSample3ServiceBeanTest
-- Depends on: test-data-proteins.sql (Protein id=1) and test-data-shipping.sql (Container id=13)

INSERT INTO Crystal (crystalId, proteinId, name, spaceGroup)
  VALUES (1, 1, 'Test Crystal', 'P212121');

-- blSampleStatus='processing' is queried by findForWSSampleInfoLight(proposalId, null, "P11", PROCESSING_STATUS)
INSERT INTO BLSample (blSampleId, crystalId, containerId, name, blSampleStatus)
  VALUES (1, 1, 13, 'Test Sample', 'processing');
