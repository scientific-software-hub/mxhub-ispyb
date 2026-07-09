-- Autoproc/scaling fixture for the CSV export integration test
-- (DataCollectionReportCsvIT). Attaches a full AutoProc -> AutoProcScaling ->
-- AutoProcScalingStatistics (innerShell/outerShell/overall) chain to the
-- DataCollection seeded by test-data-collections.sql (dataCollectionId=1),
-- so that v_datacollection_summary / getViewDataCollectionBySessionIdHavingImages
-- returns non-null completenessList / AutoProc_spaceGroups / etc. for session 1.
--
-- Values reproduce the "P 41 21 2" / outer high-resolution "1.56" example
-- already pinned by AutoProcBestResultExtractorTest and
-- DataCollectionReportBuilderTest (the client's own worked bug-report example),
-- so the integration test can assert the same numbers coming out of the real
-- DB view instead of a hand-fed map.
--
-- Depends on: test-data-collections.sql (DataCollection id=1).
--
-- Insert order follows the FK chain: AutoProcProgram -> AutoProcIntegration ->
-- AutoProc -> AutoProcScaling -> AutoProcScaling_has_Int (junction table -
-- required, without it every scaling/statistics column comes back NULL in the
-- summary view) -> AutoProcScalingStatistics (one row per shell).

INSERT INTO AutoProcProgram (autoProcProgramId, dataCollectionId, processingPrograms,
    processingStatus, processingStartTime, processingEndTime, recordTimeStamp)
  VALUES (1, 1, 'XDS', 'SUCCESS', '2021-01-01 10:05:00', '2021-01-01 10:10:00', '2021-01-01 10:10:00');

INSERT INTO AutoProcIntegration (autoProcIntegrationId, dataCollectionId, autoProcProgramId,
    startImageNumber, endImageNumber, cell_a, cell_b, cell_c, cell_alpha, cell_beta, cell_gamma,
    recordTimeStamp, anomalous)
  VALUES (1, 1, 1, 1, 100, 78.81, 78.81, 37.10, 90, 90, 90, '2021-01-01 10:10:00', 0);

INSERT INTO AutoProc (autoProcId, autoProcProgramId, spaceGroup,
    refinedCell_a, refinedCell_b, refinedCell_c, refinedCell_alpha, refinedCell_beta, refinedCell_gamma,
    recordTimeStamp)
  VALUES (1, 1, 'P 41 21 2', 78.81, 78.81, 37.10, 90, 90, 90, '2021-01-01 10:10:00');

INSERT INTO AutoProcScaling (autoProcScalingId, autoProcId, recordTimeStamp)
  VALUES (1, 1, '2021-01-01 10:10:00');

-- Junction row linking the integration to its scaling; without this the
-- summary view's autoproc/scaling columns are NULL (see comment above).
INSERT INTO AutoProcScaling_has_Int (autoProcScaling_has_IntId, autoProcScalingId, autoProcIntegrationId, recordTimeStamp)
  VALUES (1, 1, 1, '2021-01-01 10:10:00');

-- Three shells, inserted in innerShell/outerShell/overall PK order to match
-- the row order the extractor expects out of the view's GROUP_CONCAT lists.
INSERT INTO AutoProcScalingStatistics (autoProcScalingStatisticsId, autoProcScalingId, scalingStatisticsType,
    resolutionLimitLow, resolutionLimitHigh, rMerge, completeness, anomalous, recordTimeStamp)
  VALUES
    (1, 1, 'innerShell', 39.4, 6.00, 1.7,  99.6,  0, '2021-01-01 10:10:00'),
    (2, 1, 'outerShell',  1.61, 1.56, 73.5, 100.0, 0, '2021-01-01 10:10:00'),
    (3, 1, 'overall',    39.4, 1.60, 3.7,  100.0, 0, '2021-01-01 10:10:00');

-- SpaceGroup row so SpaceGroup3Service.findAll() resolves "P 41 21 2" to a
-- symmetry number (buildSpaceGroupNumberMap keys on spaceGroupName).
INSERT INTO SpaceGroup (spaceGroupId, spaceGroupNumber, spaceGroupShortName, spaceGroupName, MX_used)
  VALUES (1, 92, 'P41212', 'P 41 21 2', 1);
