-- Minimal fixture data for Proposal3ServiceBeanTest
-- Depends on: (nothing — Person has no mandatory FK)

-- person 1001 is PI of proposal 8425 only (invariant for findByLoginName("ispybdev") == 1 result)
INSERT INTO Person (personId, familyName, givenName, login) VALUES (1001, 'Dev', 'ISPyB', 'ispybdev');
-- person 1002 is PI of proposals 8426 + 20010001, and a participant in proposal 8425
INSERT INTO Person (personId, familyName, givenName, login) VALUES (1002, 'Scientist', 'Test', 'testuser');

INSERT INTO Proposal (proposalId, personId, title, proposalCode, proposalNumber, proposalType)
  VALUES (8425, 1001, 'Test Proposal MX', 'I', '20210046', 'MX');
INSERT INTO Proposal (proposalId, personId, title, proposalCode, proposalNumber, proposalType)
  VALUES (8426, 1002, 'Test Proposal MX 2', 'I', '20210047', 'MX');
-- proposal 20010001 is used by BLSample3ServiceBeanTest; personId=1002 keeps ispybdev count at 1
INSERT INTO Proposal (proposalId, personId, title, proposalCode, proposalNumber, proposalType)
  VALUES (20010001, 1002, 'BLSample Test Proposal', 'T', '20010001', 'MX');

-- person 1002 (testuser) is also a participant in proposal 8425
INSERT INTO ProposalHasPerson (proposalId, personId) VALUES (8425, 1002);
