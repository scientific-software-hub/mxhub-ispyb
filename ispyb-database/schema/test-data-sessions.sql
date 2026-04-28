-- Minimal fixture data for Session3ServiceBeanTest, BeamLineSetup3ServiceBeanTest,
-- EnergyScanRestWsServiceTest, and DewarRestWsServiceBeanTest (session-linked view).
-- Depends on: test-data-proposals.sql (Proposal 8425) and test-data-shipping.sql (Shipping 309231)

-- BeamLineSetup has no FK dependencies; required by BLSession.beamLineSetupId
INSERT INTO BeamLineSetup (beamLineSetupId) VALUES (1);

INSERT INTO BLSession (sessionId, proposalId, beamLineSetupId, beamLineName, startDate, endDate)
  VALUES (1, 8425, 1, 'P11', '2021-01-01 08:00:00', '2021-01-04 08:00:00');

-- Links Shipping 309231 to BLSession 1 so v_dewar_summary returns dewar rows by sessionId
INSERT INTO ShippingHasSession (shippingId, sessionId) VALUES (309231, 1);

-- One EnergyScan for session 1; allows EnergyScanRestWsServiceTest to return real rows
INSERT INTO EnergyScan (energyScanId, sessionId, element) VALUES (1, 1, 'Se');
