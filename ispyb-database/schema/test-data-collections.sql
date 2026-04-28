-- Minimal fixture data for DataCollectionGroup3ServiceBeanTest and DataCollection3ServiceBeanTest
-- Depends on: test-data-sessions.sql (BLSession id=1) and test-data-blsample.sql (BLSample id=1)

-- sessionId is NOT NULL; blSampleId is optional (we include it to test findBySampleId)
INSERT INTO DataCollectionGroup (dataCollectionGroupId, sessionId, blSampleId, experimentType)
  VALUES (1, 1, 1, 'OSC');

-- Only dataCollectionId and dataCollectionGroupId are NOT NULL; all others nullable.
-- numberOfImages=100 → counted as a real collect (> 4), not a test (≤ 4).
INSERT INTO DataCollection (dataCollectionId, dataCollectionGroupId, imageDirectory,
    imagePrefix, dataCollectionNumber, numberOfImages, startTime)
  VALUES (1, 1, '/data/test/', 'test_', 1, 100, '2021-01-01 10:00:00');
