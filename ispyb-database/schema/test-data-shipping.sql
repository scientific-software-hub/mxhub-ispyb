-- Minimal fixture data for Shipping3ServiceBeanTest and Container3ServiceBeanTest
-- Depends on: test-data-proposals.sql (Proposal 8425 must exist)

INSERT INTO Shipping (shippingId, proposalId, shippingName, shippingStatus)
  VALUES (309231, 8425, 'Test Shipping', 'opened');

INSERT INTO Dewar (dewarId, shippingId, code, dewarStatus)
  VALUES (1, 309231, 'TEST-DEWAR-01', 'opened');

-- containerId=13 matches the hardcoded ID used in Container3ServiceBeanTest
INSERT INTO Container (containerId, dewarId, code, containerType, capacity, sampleChangerLocation)
  VALUES (13, 1, 'TEST-CONTAINER-01', 'Puck', 16, 'SC01');
