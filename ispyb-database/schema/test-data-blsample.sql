-- Minimal fixture data for BLSample3ServiceBeanTest
-- Depends on: test-data-proposals.sql (Proposal 20010001) and test-data-shipping.sql (Container 13)

INSERT INTO Protein (proteinId, proposalId, name, acronym)
  VALUES (1, 20010001, 'Test Protein', 'TST');

INSERT INTO Crystal (crystalId, proteinId, name, spaceGroup)
  VALUES (1, 1, 'Test Crystal', 'P212121');

-- blSampleStatus='processing' is queried by findForWSSampleInfoLight(proposalId, null, "P11", PROCESSING_STATUS)
INSERT INTO BLSample (blSampleId, crystalId, containerId, name, blSampleStatus)
  VALUES (1, 1, 13, 'Test Sample', 'processing');
