select *, 
(select count(*) from EnergyScan where EnergyScan.sessionId = v_session.sessionId) as energyScanCount,
(select count(distinct(blSampleId)) from DataCollectionGroup where DataCollectionGroup.sessionId = v_session.sessionId) as sampleCount,
(select sum(DataCollection.numberOfImages) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId) as imagesCount,
(select count(*) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId and DataCollection.numberOfImages < 5) as testDataCollectionGroupCount,
(select count(*) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId and DataCollection.numberOfImages > 4) as dataCollectionGroupCount,
(select count(*) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId and DataCollectionGroup.experimentType = 'EM') as EMdataCollectionGroupCount,
(select count(*) from XFEFluorescenceSpectrum where XFEFluorescenceSpectrum.sessionId = v_session.sessionId) as xrfSpectrumCount,
(select count(*) from Experiment exp1 where v_session.sessionId = exp1.sessionId and exp1.experimentType='HPLC') as hplcCount,
(select count(*) from Experiment exp2 where v_session.sessionId = exp2.sessionId and exp2.experimentType='STATIC') as sampleChangerCount,
(select count(*) from Experiment exp3 where v_session.sessionId = exp3.sessionId and exp3.experimentType='CALIBRATION') as calibrationCount,
(select experimentType from DataCollectionGroup where DataCollectionGroup.dataCollectionGroupId = (select max(dataCollectionGroupId) from DataCollectionGroup dg2 where  dg2.sessionId = v_session.sessionId))  as lastExperimentDataCollectionGroup,
(select startTime from DataCollectionGroup where DataCollectionGroup.dataCollectionGroupId = (select min(dataCollectionGroupId) from DataCollectionGroup dg2 where  dg2.sessionId = v_session.sessionId))  as firstStartTimeDataCollectionGroup,
(select endTime from DataCollectionGroup where DataCollectionGroup.dataCollectionGroupId = (select max(dataCollectionGroupId) from DataCollectionGroup dg2 where  dg2.sessionId = v_session.sessionId))  as lastEndTimeDataCollectionGroup,
(select TIMESTAMPDIFF(SECOND, MIN(dcg.startTime), MAX(dcg.endTime)) from DataCollectionGroup dcg where dcg.sessionId = v_session.sessionId)
- COALESCE((select SUM(TIMESTAMPDIFF(SECOND, prev.endTime, curr.startTime)) from DataCollectionGroup curr join DataCollectionGroup prev on prev.dataCollectionGroupId = (select MAX(p.dataCollectionGroupId) from DataCollectionGroup p where p.sessionId = curr.sessionId and p.dataCollectionGroupId < curr.dataCollectionGroupId) where curr.sessionId = v_session.sessionId and TIMESTAMPDIFF(SECOND, prev.endTime, curr.startTime) > 900), 0) as netDataCollectionTimeInSeconds
from v_session