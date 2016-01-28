CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`V_datacollection_summary` AS
    SELECT 
        (SELECT 
                COUNT(0)
            FROM
                `pydb`.`Image`
            WHERE
                (`pydb`.`Image`.`dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)) AS `nbStoredImages`,
        (SELECT 
                MIN(`pydb`.`Image`.`imageId`)
            FROM
                `pydb`.`Image`
            WHERE
                (`pydb`.`Image`.`dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)) AS `firstImageId`,
        (SELECT 
                MAX(`pydb`.`Image`.`imageId`)
            FROM
                `pydb`.`Image`
            WHERE
                (`pydb`.`Image`.`dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)) AS `lastImageId`,
        `pydb`.`DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,
        `pydb`.`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,
        `pydb`.`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,
        `pydb`.`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,
        `pydb`.`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,
        `pydb`.`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,
        `pydb`.`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,
        `pydb`.`DataCollectionGroup`.`crystalClass` AS `DataCollectionGroup_crystalClass`,
        `pydb`.`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,
        `pydb`.`DataCollectionGroup`.`detectorMode` AS `DataCollectionGroup_detectorMode`,
        `pydb`.`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,
        `pydb`.`DataCollectionGroup`.`actualSampleSlotInContainer` AS `DataCollectionGroup_actualSampleSlotInContainer`,
        `pydb`.`DataCollectionGroup`.`actualContainerBarcode` AS `DataCollectionGroup_actualContainerBarcode`,
        `pydb`.`DataCollectionGroup`.`actualContainerSlotInSC` AS `DataCollectionGroup_actualContainerSlotInSC`,
        `pydb`.`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,
        `pydb`.`Screening`.`screeningId` AS `Screening_screeningId`,
        `pydb`.`Screening`.`diffractionPlanId` AS `Screening_diffractionPlanId`,
        `pydb`.`Screening`.`dataCollectionId` AS `Screening_dataCollectionId`,
        `pydb`.`Screening`.`bltimeStamp` AS `Screening_bltimeStamp`,
        `pydb`.`Screening`.`programVersion` AS `Screening_programVersion`,
        `pydb`.`Screening`.`comments` AS `Screening_comments`,
        `pydb`.`Screening`.`shortComments` AS `Screening_shortComments`,
        `pydb`.`Screening`.`xmlSampleInformation` AS `Screening_xmlSampleInformation`,
        `pydb`.`ScreeningOutput`.`screeningOutputId` AS `ScreeningOutput_screeningOutputId`,
        `pydb`.`ScreeningOutput`.`screeningId` AS `ScreeningOutput_screeningId`,
        `pydb`.`ScreeningOutput`.`statusDescription` AS `ScreeningOutput_statusDescription`,
        `pydb`.`ScreeningOutput`.`rejectedReflections` AS `ScreeningOutput_rejectedReflections`,
        `pydb`.`ScreeningOutput`.`resolutionObtained` AS `ScreeningOutput_resolutionObtained`,
        `pydb`.`ScreeningOutput`.`spotDeviationR` AS `ScreeningOutput_spotDeviationR`,
        `pydb`.`ScreeningOutput`.`spotDeviationTheta` AS `ScreeningOutput_spotDeviationTheta`,
        `pydb`.`ScreeningOutput`.`beamShiftX` AS `ScreeningOutput_beamShiftX`,
        `pydb`.`ScreeningOutput`.`beamShiftY` AS `ScreeningOutput_beamShiftY`,
        `pydb`.`ScreeningOutput`.`numSpotsFound` AS `ScreeningOutput_numSpotsFound`,
        `pydb`.`ScreeningOutput`.`numSpotsUsed` AS `ScreeningOutput_numSpotsUsed`,
        `pydb`.`ScreeningOutput`.`numSpotsRejected` AS `ScreeningOutput_numSpotsRejected`,
        `pydb`.`ScreeningOutput`.`mosaicity` AS `ScreeningOutput_mosaicity`,
        `pydb`.`ScreeningOutput`.`iOverSigma` AS `ScreeningOutput_iOverSigma`,
        `pydb`.`ScreeningOutput`.`diffractionRings` AS `ScreeningOutput_diffractionRings`,
        `pydb`.`ScreeningOutput`.`strategySuccess` AS `ScreeningOutput_strategySuccess`,
        `pydb`.`ScreeningOutput`.`mosaicityEstimated` AS `ScreeningOutput_mosaicityEstimated`,
        `pydb`.`ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`,
        `pydb`.`ScreeningOutput`.`program` AS `ScreeningOutput_program`,
        `pydb`.`ScreeningOutput`.`doseTotal` AS `ScreeningOutput_doseTotal`,
        `pydb`.`ScreeningOutput`.`totalExposureTime` AS `ScreeningOutput_totalExposureTime`,
        `pydb`.`ScreeningOutput`.`totalRotationRange` AS `ScreeningOutput_totalRotationRange`,
        `pydb`.`ScreeningOutput`.`totalNumberOfImages` AS `ScreeningOutput_totalNumberOfImages`,
        `pydb`.`ScreeningOutput`.`rFriedel` AS `ScreeningOutput_rFriedel`,
        `pydb`.`ScreeningOutput`.`indexingSuccess` AS `ScreeningOutput_indexingSuccess`,
        `pydb`.`ScreeningStrategy`.`screeningStrategyId` AS `ScreeningStrategy_screeningStrategyId`,
        `pydb`.`ScreeningStrategy`.`screeningOutputId` AS `ScreeningStrategy_screeningOutputId`,
        `pydb`.`ScreeningStrategy`.`phiStart` AS `ScreeningStrategy_phiStart`,
        `pydb`.`ScreeningStrategy`.`phiEnd` AS `ScreeningStrategy_phiEnd`,
        `pydb`.`ScreeningStrategy`.`rotation` AS `ScreeningStrategy_rotation`,
        `pydb`.`ScreeningStrategy`.`exposureTime` AS `ScreeningStrategy_exposureTime`,
        `pydb`.`ScreeningStrategy`.`resolution` AS `ScreeningStrategy_resolution`,
        `pydb`.`ScreeningStrategy`.`completeness` AS `ScreeningStrategy_completeness`,
        `pydb`.`ScreeningStrategy`.`multiplicity` AS `ScreeningStrategy_multiplicity`,
        `pydb`.`ScreeningStrategy`.`anomalous` AS `ScreeningStrategy_anomalous`,
        `pydb`.`ScreeningStrategy`.`program` AS `ScreeningStrategy_program`,
        `pydb`.`ScreeningStrategy`.`rankingResolution` AS `ScreeningStrategy_rankingResolution`,
        `pydb`.`ScreeningStrategy`.`transmission` AS `ScreeningStrategy_transmission`,
        `pydb`.`BLSample`.`blSampleId` AS `BLSample_blSampleId`,
        `pydb`.`BLSample`.`diffractionPlanId` AS `BLSample_diffractionPlanId`,
        `pydb`.`BLSample`.`crystalId` AS `BLSample_crystalId`,
        `pydb`.`BLSample`.`containerId` AS `BLSample_containerId`,
        `pydb`.`BLSample`.`name` AS `BLSample_name`,
        `pydb`.`BLSample`.`code` AS `BLSample_code`,
        `pydb`.`BLSample`.`location` AS `BLSample_location`,
        `pydb`.`BLSample`.`holderLength` AS `BLSample_holderLength`,
        `pydb`.`BLSample`.`loopLength` AS `BLSample_loopLength`,
        `pydb`.`BLSample`.`loopType` AS `BLSample_loopType`,
        `pydb`.`BLSample`.`wireWidth` AS `BLSample_wireWidth`,
        `pydb`.`BLSample`.`comments` AS `BLSample_comments`,
        `pydb`.`BLSample`.`completionStage` AS `BLSample_completionStage`,
        `pydb`.`BLSample`.`structureStage` AS `BLSample_structureStage`,
        `pydb`.`BLSample`.`publicationStage` AS `BLSample_publicationStage`,
        `pydb`.`BLSample`.`publicationComments` AS `BLSample_publicationComments`,
        `pydb`.`BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`,
        `pydb`.`BLSample`.`isInSampleChanger` AS `BLSample_isInSampleChanger`,
        `pydb`.`BLSample`.`lastKnownCenteringPosition` AS `BLSample_lastKnownCenteringPosition`,
        `pydb`.`BLSample`.`recordTimeStamp` AS `BLSample_recordTimeStamp`,
        `pydb`.`BLSample`.`SMILES` AS `BLSample_SMILES`,
        `pydb`.`Crystal`.`crystalId` AS `Crystal_crystalId`,
        `pydb`.`Crystal`.`diffractionPlanId` AS `Crystal_diffractionPlanId`,
        `pydb`.`Crystal`.`proteinId` AS `Crystal_proteinId`,
        `pydb`.`Crystal`.`crystalUUID` AS `Crystal_crystalUUID`,
        `pydb`.`Crystal`.`name` AS `Crystal_name`,
        `pydb`.`Crystal`.`spaceGroup` AS `Crystal_spaceGroup`,
        `pydb`.`Crystal`.`morphology` AS `Crystal_morphology`,
        `pydb`.`Crystal`.`color` AS `Crystal_color`,
        `pydb`.`Crystal`.`size_X` AS `Crystal_size_X`,
        `pydb`.`Crystal`.`size_Y` AS `Crystal_size_Y`,
        `pydb`.`Crystal`.`size_Z` AS `Crystal_size_Z`,
        `pydb`.`Crystal`.`cell_a` AS `Crystal_cell_a`,
        `pydb`.`Crystal`.`cell_b` AS `Crystal_cell_b`,
        `pydb`.`Crystal`.`cell_c` AS `Crystal_cell_c`,
        `pydb`.`Crystal`.`cell_alpha` AS `Crystal_cell_alpha`,
        `pydb`.`Crystal`.`cell_beta` AS `Crystal_cell_beta`,
        `pydb`.`Crystal`.`cell_gamma` AS `Crystal_cell_gamma`,
        `pydb`.`Crystal`.`comments` AS `Crystal_comments`,
        `pydb`.`Crystal`.`pdbFileName` AS `Crystal_pdbFileName`,
        `pydb`.`Crystal`.`pdbFilePath` AS `Crystal_pdbFilePath`,
        `pydb`.`Crystal`.`recordTimeStamp` AS `Crystal_recordTimeStamp`,
        `pydb`.`BLSession`.`sessionId` AS `BLSession_sessionId`,
        `pydb`.`BLSession`.`expSessionPk` AS `BLSession_expSessionPk`,
        `pydb`.`BLSession`.`beamLineSetupId` AS `BLSession_beamLineSetupId`,
        `pydb`.`BLSession`.`proposalId` AS `BLSession_proposalId`,
        `pydb`.`BLSession`.`projectCode` AS `BLSession_projectCode`,
        `pydb`.`BLSession`.`startDate` AS `BLSession_startDate`,
        `pydb`.`BLSession`.`endDate` AS `BLSession_endDate`,
        `pydb`.`BLSession`.`beamLineName` AS `BLSession_beamLineName`,
        `pydb`.`BLSession`.`scheduled` AS `BLSession_scheduled`,
        `pydb`.`BLSession`.`nbShifts` AS `BLSession_nbShifts`,
        `pydb`.`BLSession`.`comments` AS `BLSession_comments`,
        `pydb`.`BLSession`.`beamLineOperator` AS `BLSession_beamLineOperator`,
        `pydb`.`BLSession`.`visit_number` AS `BLSession_visit_number`,
        `pydb`.`BLSession`.`bltimeStamp` AS `BLSession_bltimeStamp`,
        `pydb`.`BLSession`.`usedFlag` AS `BLSession_usedFlag`,
        `pydb`.`BLSession`.`sessionTitle` AS `BLSession_sessionTitle`,
        `pydb`.`BLSession`.`structureDeterminations` AS `BLSession_structureDeterminations`,
        `pydb`.`BLSession`.`dewarTransport` AS `BLSession_dewarTransport`,
        `pydb`.`BLSession`.`databackupFrance` AS `BLSession_databackupFrance`,
        `pydb`.`BLSession`.`databackupEurope` AS `BLSession_databackupEurope`,
        `pydb`.`BLSession`.`operatorSiteNumber` AS `BLSession_operatorSiteNumber`,
        `pydb`.`BLSession`.`lastUpdate` AS `BLSession_lastUpdate`,
        `pydb`.`BLSession`.`protectedData` AS `BLSession_protectedData`,
        `pydb`.`Protein`.`proteinId` AS `Protein_proteinId`,
        `pydb`.`Protein`.`proposalId` AS `Protein_proposalId`,
        `pydb`.`Protein`.`name` AS `Protein_name`,
        `pydb`.`Protein`.`acronym` AS `Protein_acronym`,
        `pydb`.`Protein`.`molecularMass` AS `Protein_molecularMass`,
        `pydb`.`Protein`.`proteinType` AS `Protein_proteinType`,
        `pydb`.`Protein`.`sequence` AS `Protein_sequence`,
        `pydb`.`Protein`.`personId` AS `Protein_personId`,
        `pydb`.`Protein`.`bltimeStamp` AS `Protein_bltimeStamp`,
        `pydb`.`Protein`.`isCreatedBySampleSheet` AS `Protein_isCreatedBySampleSheet`,
        `pydb`.`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,
        `pydb`.`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,
        `pydb`.`DataCollection`.`strategySubWedgeOrigId` AS `DataCollection_strategySubWedgeOrigId`,
        `pydb`.`DataCollection`.`detectorId` AS `DataCollection_detectorId`,
        `pydb`.`DataCollection`.`blSubSampleId` AS `DataCollection_blSubSampleId`,
        `pydb`.`DataCollection`.`startPositionId` AS `DataCollection_startPositionId`,
        `pydb`.`DataCollection`.`endPositionId` AS `DataCollection_endPositionId`,
        `pydb`.`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,
        `pydb`.`DataCollection`.`startTime` AS `DataCollection_startTime`,
        `pydb`.`DataCollection`.`endTime` AS `DataCollection_endTime`,
        `pydb`.`DataCollection`.`runStatus` AS `DataCollection_runStatus`,
        `pydb`.`DataCollection`.`axisStart` AS `DataCollection_axisStart`,
        `pydb`.`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,
        `pydb`.`DataCollection`.`axisRange` AS `DataCollection_axisRange`,
        `pydb`.`DataCollection`.`overlap` AS `DataCollection_overlap`,
        `pydb`.`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,
        `pydb`.`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,
        `pydb`.`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,
        `pydb`.`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,
        `pydb`.`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,
        `pydb`.`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`,
        `pydb`.`DataCollection`.`imageSuffix` AS `DataCollection_imageSuffix`,
        `pydb`.`DataCollection`.`fileTemplate` AS `DataCollection_fileTemplate`,
        `pydb`.`DataCollection`.`wavelength` AS `DataCollection_wavelength`,
        `pydb`.`DataCollection`.`resolution` AS `DataCollection_resolution`,
        `pydb`.`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,
        `pydb`.`DataCollection`.`xBeam` AS `DataCollection_xBeam`,
        `pydb`.`DataCollection`.`yBeam` AS `DataCollection_yBeam`,
        `pydb`.`DataCollection`.`comments` AS `DataCollection_comments`,
        `pydb`.`DataCollection`.`printableForReport` AS `DataCollection_printableForReport`,
        `pydb`.`DataCollection`.`slitGapVertical` AS `DataCollection_slitGapVertical`,
        `pydb`.`DataCollection`.`slitGapHorizontal` AS `DataCollection_slitGapHorizontal`,
        `pydb`.`DataCollection`.`transmission` AS `DataCollection_transmission`,
        `pydb`.`DataCollection`.`synchrotronMode` AS `DataCollection_synchrotronMode`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,
        `pydb`.`DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`,
        `pydb`.`DataCollection`.`phiStart` AS `DataCollection_phiStart`,
        `pydb`.`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,
        `pydb`.`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,
        `pydb`.`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,
        `pydb`.`DataCollection`.`detector2Theta` AS `DataCollection_detector2Theta`,
        `pydb`.`DataCollection`.`undulatorGap1` AS `DataCollection_undulatorGap1`,
        `pydb`.`DataCollection`.`undulatorGap2` AS `DataCollection_undulatorGap2`,
        `pydb`.`DataCollection`.`undulatorGap3` AS `DataCollection_undulatorGap3`,
        `pydb`.`DataCollection`.`beamSizeAtSampleX` AS `DataCollection_beamSizeAtSampleX`,
        `pydb`.`DataCollection`.`beamSizeAtSampleY` AS `DataCollection_beamSizeAtSampleY`,
        `pydb`.`DataCollection`.`centeringMethod` AS `DataCollection_centeringMethod`,
        `pydb`.`DataCollection`.`averageTemperature` AS `DataCollection_averageTemperature`,
        `pydb`.`DataCollection`.`actualCenteringPosition` AS `DataCollection_actualCenteringPosition`,
        `pydb`.`DataCollection`.`beamShape` AS `DataCollection_beamShape`,
        `pydb`.`DataCollection`.`flux` AS `DataCollection_flux`,
        `pydb`.`DataCollection`.`flux_end` AS `DataCollection_flux_end`,
        `pydb`.`DataCollection`.`totalAbsorbedDose` AS `DataCollection_totalAbsorbedDose`,
        `pydb`.`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,
        `pydb`.`Detector`.`detectorId` AS `Detector_detectorId`,
        `pydb`.`Detector`.`detectorType` AS `Detector_detectorType`,
        `pydb`.`Detector`.`detectorManufacturer` AS `Detector_detectorManufacturer`,
        `pydb`.`Detector`.`detectorModel` AS `Detector_detectorModel`,
        `pydb`.`Detector`.`detectorPixelSizeHorizontal` AS `Detector_detectorPixelSizeHorizontal`,
        `pydb`.`Detector`.`detectorPixelSizeVertical` AS `Detector_detectorPixelSizeVertical`,
        `pydb`.`Detector`.`detectorSerialNumber` AS `Detector_detectorSerialNumber`,
        `pydb`.`Detector`.`detectorDistanceMin` AS `Detector_detectorDistanceMin`,
        `pydb`.`Detector`.`detectorDistanceMax` AS `Detector_detectorDistanceMax`,
        `pydb`.`Detector`.`trustedPixelValueRangeLower` AS `Detector_trustedPixelValueRangeLower`,
        `pydb`.`Detector`.`trustedPixelValueRangeUpper` AS `Detector_trustedPixelValueRangeUpper`,
        `pydb`.`Detector`.`sensorThickness` AS `Detector_sensorThickness`,
        `pydb`.`Detector`.`overload` AS `Detector_overload`,
        `pydb`.`Detector`.`XGeoCorr` AS `Detector_XGeoCorr`,
        `pydb`.`Detector`.`YGeoCorr` AS `Detector_YGeoCorr`,
        `pydb`.`Detector`.`detectorMode` AS `Detector_detectorMode`,
        `pydb`.`AutoProcIntegration`.`startImageNumber` AS `AutoProcIntegration_startImageNumber`,
        `pydb`.`AutoProcIntegration`.`endImageNumber` AS `AutoProcIntegration_endImageNumber`,
        `pydb`.`PhasingStep`.`phasingStepType` AS `PhasingStep_phasingStepType`,
        `pydb`.`PhasingStep`.`method` AS `PhasingStep_method`
    FROM
        ((((((((((((((`pydb`.`DataCollectionGroup`
        LEFT JOIN `pydb`.`DataCollection` ON ((`pydb`.`DataCollection`.`dataCollectionGroupId` = `pydb`.`DataCollectionGroup`.`dataCollectionGroupId`)))
        LEFT JOIN `pydb`.`Detector` ON ((`pydb`.`DataCollection`.`detectorId` = `pydb`.`Detector`.`detectorId`)))
        LEFT JOIN `pydb`.`Screening` ON ((`pydb`.`Screening`.`dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `pydb`.`ScreeningOutput` ON ((`pydb`.`ScreeningOutput`.`screeningId` = `pydb`.`Screening`.`screeningId`)))
        LEFT JOIN `pydb`.`ScreeningStrategy` ON ((`pydb`.`ScreeningStrategy`.`screeningOutputId` = `pydb`.`ScreeningOutput`.`screeningOutputId`)))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `pydb`.`BLSample` ON ((`pydb`.`BLSample`.`blSampleId` = `pydb`.`DataCollectionGroup`.`blSampleId`)))
        LEFT JOIN `pydb`.`Crystal` ON ((`pydb`.`Crystal`.`crystalId` = `pydb`.`BLSample`.`crystalId`)))
        LEFT JOIN `pydb`.`Protein` ON ((`pydb`.`Protein`.`proteinId` = `pydb`.`Crystal`.`proteinId`)))
        LEFT JOIN `pydb`.`AutoProcIntegration` ON ((`pydb`.`AutoProcIntegration`.`dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)))
        LEFT JOIN `pydb`.`AutoProcScaling_has_Int` ON ((`pydb`.`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `pydb`.`AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `pydb`.`AutoProcScaling` ON ((`pydb`.`AutoProcScaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling_has_Int`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`Phasing_has_Scaling` ON ((`pydb`.`Phasing_has_Scaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`PhasingStep` ON ((`pydb`.`PhasingStep`.`autoProcScalingId` = `pydb`.`Phasing_has_Scaling`.`autoProcScalingId`)))