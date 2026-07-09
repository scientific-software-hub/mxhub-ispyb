-- Minimal fixture data for Protein3ServiceBeanTest and Crystal3ServiceBeanTest
-- Depends on: test-data-proposals.sql (Proposals 20010001 and 8425 must exist)

-- Protein id=1 has a Crystal (id=1) — used by findByPk(1, withCrystals=true) and BLSample tests
INSERT INTO Protein (proteinId, proposalId, name, acronym)
  VALUES (1, 20010001, 'Test Protein', 'TST');

-- Protein id=2 has NO crystals — used by findByPk(2, withCrystals=false) and findByProposalId(8425)
INSERT INTO Protein (proteinId, proposalId, name, acronym)
  VALUES (2, 8425, 'MX Protein', 'MXP');
