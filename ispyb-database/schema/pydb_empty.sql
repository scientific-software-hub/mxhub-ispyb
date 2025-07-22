-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Jul 22, 2025 at 07:53 PM
-- Server version: 11.3.2-MariaDB-1:11.3.2+maria~ubu2204
-- PHP Version: 8.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pydb`
--
CREATE DATABASE IF NOT EXISTS `pydb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `pydb`;

-- --------------------------------------------------------

--
-- Table structure for table `AbInitioModel`
--

CREATE TABLE `AbInitioModel` (
  `abInitioModelId` int(10) NOT NULL,
  `modelListId` int(10) DEFAULT NULL,
  `averagedModelId` int(10) DEFAULT NULL,
  `rapidShapeDeterminationModelId` int(10) DEFAULT NULL,
  `shapeDeterminationModelId` int(10) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Additive`
--

CREATE TABLE `Additive` (
  `additiveId` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `additiveType` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `chemFormulaHead` varchar(25) DEFAULT '',
  `chemFormulaTail` varchar(25) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AdminActivity`
--

CREATE TABLE `AdminActivity` (
  `adminActivityId` int(11) NOT NULL,
  `username` varchar(45) NOT NULL DEFAULT '',
  `action` varchar(45) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `dateTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AdminVar`
--

CREATE TABLE `AdminVar` (
  `varId` int(11) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `value` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='ISPyB administration values';

-- --------------------------------------------------------

--
-- Table structure for table `Aperture`
--

CREATE TABLE `Aperture` (
  `apertureId` int(10) UNSIGNED NOT NULL,
  `sizeX` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Assembly`
--

CREATE TABLE `Assembly` (
  `assemblyId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AssemblyHasMacromolecule`
--

CREATE TABLE `AssemblyHasMacromolecule` (
  `AssemblyHasMacromoleculeId` int(10) NOT NULL,
  `assemblyId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AssemblyRegion`
--

CREATE TABLE `AssemblyRegion` (
  `assemblyRegionId` int(10) NOT NULL,
  `assemblyHasMacromoleculeId` int(10) NOT NULL,
  `assemblyRegionType` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `fromResiduesBases` varchar(45) DEFAULT NULL,
  `toResiduesBases` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProc`
--

CREATE TABLE `AutoProc` (
  `autoProcId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcProgramId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related program item',
  `spaceGroup` varchar(45) DEFAULT NULL COMMENT 'Space group',
  `refinedCell_a` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_b` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_c` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_alpha` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_beta` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_gamma` float DEFAULT NULL COMMENT 'Refined cell',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcIntegration`
--

CREATE TABLE `AutoProcIntegration` (
  `autoProcIntegrationId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `dataCollectionId` int(11) UNSIGNED NOT NULL COMMENT 'DataCollection item',
  `autoProcProgramId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related program item',
  `startImageNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'start image number',
  `endImageNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'end image number',
  `refinedDetectorDistance` float DEFAULT NULL COMMENT 'Refined DataCollection.detectorDistance',
  `refinedXBeam` float DEFAULT NULL COMMENT 'Refined DataCollection.xBeam',
  `refinedYBeam` float DEFAULT NULL COMMENT 'Refined DataCollection.yBeam',
  `rotationAxisX` float DEFAULT NULL COMMENT 'Rotation axis',
  `rotationAxisY` float DEFAULT NULL COMMENT 'Rotation axis',
  `rotationAxisZ` float DEFAULT NULL COMMENT 'Rotation axis',
  `beamVectorX` float DEFAULT NULL COMMENT 'Beam vector',
  `beamVectorY` float DEFAULT NULL COMMENT 'Beam vector',
  `beamVectorZ` float DEFAULT NULL COMMENT 'Beam vector',
  `cell_a` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_b` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_c` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_alpha` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_beta` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_gamma` float DEFAULT NULL COMMENT 'Unit cell',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `anomalous` tinyint(1) DEFAULT 0 COMMENT 'boolean type:0 noanoum - 1 anoum'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcProgram`
--

CREATE TABLE `AutoProcProgram` (
  `autoProcProgramId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `dataCollectionId` int(11) UNSIGNED DEFAULT NULL,
  `processingCommandLine` varchar(255) DEFAULT NULL COMMENT 'Command line for running the automatic processing',
  `processingPrograms` varchar(255) DEFAULT NULL COMMENT 'Processing programs (comma separated)',
  `processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1') DEFAULT NULL COMMENT 'success (1) / fail (0)',
  `processingMessage` varchar(255) DEFAULT NULL COMMENT 'warning, error,...',
  `processingStartTime` datetime DEFAULT NULL COMMENT 'Processing start time',
  `processingEndTime` datetime DEFAULT NULL COMMENT 'Processing end time',
  `processingEnvironment` varchar(255) DEFAULT NULL COMMENT 'Cpus, Nodes,...',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcProgramAttachment`
--

CREATE TABLE `AutoProcProgramAttachment` (
  `autoProcProgramAttachmentId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcProgramId` int(10) UNSIGNED NOT NULL COMMENT 'Related autoProcProgram item',
  `fileType` enum('Log','Result','Graph') DEFAULT NULL COMMENT 'Type of file Attachment',
  `fileName` varchar(255) DEFAULT NULL COMMENT 'Attachment filename',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'Attachment filepath to disk storage',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScaling`
--

CREATE TABLE `AutoProcScaling` (
  `autoProcScalingId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related autoProc item (used by foreign key)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `resolutionEllipsoidAxis11` float DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 1',
  `resolutionEllipsoidAxis12` float DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 2',
  `resolutionEllipsoidAxis13` float DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 3',
  `resolutionEllipsoidAxis21` float DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 1',
  `resolutionEllipsoidAxis23` float DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 3',
  `resolutionEllipsoidAxis31` float DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 1',
  `resolutionEllipsoidAxis32` float DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 2',
  `resolutionEllipsoidAxis33` float DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 3',
  `resolutionEllipsoidValue1` float DEFAULT NULL COMMENT 'First (anisotropic) diffraction limit',
  `resolutionEllipsoidValue2` float DEFAULT NULL COMMENT 'Second (anisotropic) diffraction limit',
  `resolutionEllipsoidValue3` float DEFAULT NULL COMMENT 'Third (anisotropic) diffraction limit',
  `resolutionEllipsoidAxis22` float DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 2'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScalingStatistics`
--

CREATE TABLE `AutoProcScalingStatistics` (
  `autoProcScalingStatisticsId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcScalingId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related autoProcScaling item (used by foreign key)',
  `scalingStatisticsType` enum('overall','innerShell','outerShell') NOT NULL DEFAULT 'overall' COMMENT 'Scaling statistics type',
  `comments` varchar(255) DEFAULT NULL COMMENT 'Comments...',
  `resolutionLimitLow` float DEFAULT NULL COMMENT 'Low resolution limit',
  `resolutionLimitHigh` float DEFAULT NULL COMMENT 'High resolution limit',
  `rMerge` float DEFAULT NULL COMMENT 'Rmerge',
  `rMeasWithinIPlusIMinus` float DEFAULT NULL COMMENT 'Rmeas (within I+/I-)',
  `rMeasAllIPlusIMinus` float DEFAULT NULL COMMENT 'Rmeas (all I+ & I-)',
  `rPimWithinIPlusIMinus` float DEFAULT NULL COMMENT 'Rpim (within I+/I-) ',
  `rPimAllIPlusIMinus` float DEFAULT NULL COMMENT 'Rpim (all I+ & I-)',
  `fractionalPartialBias` float DEFAULT NULL COMMENT 'Fractional partial bias',
  `nTotalObservations` int(10) DEFAULT NULL COMMENT 'Total number of observations',
  `nTotalUniqueObservations` int(10) DEFAULT NULL COMMENT 'Total number unique',
  `meanIOverSigI` float DEFAULT NULL COMMENT 'Mean((I)/sd(I))',
  `completeness` float DEFAULT NULL COMMENT 'Completeness',
  `multiplicity` float DEFAULT NULL COMMENT 'Multiplicity',
  `anomalousCompleteness` float DEFAULT NULL COMMENT 'Anomalous completeness',
  `anomalousMultiplicity` float DEFAULT NULL COMMENT 'Anomalous multiplicity',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `anomalous` tinyint(1) DEFAULT 0 COMMENT 'boolean type:0 noanoum - 1 anoum',
  `ccHalf` float DEFAULT NULL COMMENT 'information from XDS',
  `ccAno` float DEFAULT NULL,
  `sigAno` varchar(45) DEFAULT NULL,
  `isa` varchar(45) DEFAULT NULL,
  `completenessSpherical` float DEFAULT NULL COMMENT 'Completeness calculated assuming isotropic diffraction',
  `completenessEllipsoidal` float DEFAULT NULL COMMENT 'Completeness calculated allowing for anisotropic diffraction',
  `anomalousCompletenessSpherical` float DEFAULT NULL COMMENT 'Anomalous completeness calculated assuming isotropic diffraction',
  `anomalousCompletenessEllipsoidal` float DEFAULT NULL COMMENT 'Anisotropic completeness calculated allowing for anisotropic diffraction'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScaling_has_Int`
--

CREATE TABLE `AutoProcScaling_has_Int` (
  `autoProcScaling_has_IntId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcScalingId` int(10) UNSIGNED DEFAULT NULL COMMENT 'AutoProcScaling item',
  `autoProcIntegrationId` int(10) UNSIGNED NOT NULL COMMENT 'AutoProcIntegration item',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcStatus`
--

CREATE TABLE `AutoProcStatus` (
  `autoProcStatusId` int(11) NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcIntegrationId` int(10) UNSIGNED NOT NULL,
  `step` enum('Indexing','Integration','Correction','Scaling','Importing') NOT NULL COMMENT 'autoprocessing step',
  `status` enum('Launched','Successful','Failed') NOT NULL COMMENT 'autoprocessing status',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `bltimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='AutoProcStatus table is linked to AutoProcIntegration';

-- --------------------------------------------------------

--
-- Table structure for table `BeamApertures`
--

CREATE TABLE `BeamApertures` (
  `beamAperturesid` int(11) UNSIGNED NOT NULL,
  `beamlineStatsId` int(11) UNSIGNED DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `apertureSize` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BeamCalendar`
--

CREATE TABLE `BeamCalendar` (
  `beamCalendarId` int(10) UNSIGNED NOT NULL,
  `run` varchar(7) NOT NULL,
  `beamStatus` varchar(24) NOT NULL,
  `startDate` datetime NOT NULL,
  `endDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BeamCentres`
--

CREATE TABLE `BeamCentres` (
  `beamCentresid` int(11) UNSIGNED NOT NULL,
  `beamlineStatsId` int(11) UNSIGNED DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `zoom` tinyint(3) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BeamlineAction`
--

CREATE TABLE `BeamlineAction` (
  `beamlineActionId` int(11) UNSIGNED NOT NULL,
  `sessionId` int(11) UNSIGNED DEFAULT NULL,
  `startTimestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `endTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `message` varchar(255) DEFAULT NULL,
  `parameter` varchar(50) DEFAULT NULL,
  `value` varchar(30) DEFAULT NULL,
  `loglevel` enum('DEBUG','CRITICAL','INFO') DEFAULT NULL,
  `status` enum('PAUSED','RUNNING','TERMINATED','COMPLETE','ERROR','EPICSFAIL') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BeamLineSetup`
--

CREATE TABLE `BeamLineSetup` (
  `beamLineSetupId` int(10) UNSIGNED NOT NULL,
  `synchrotronMode` varchar(255) DEFAULT NULL,
  `undulatorType1` varchar(45) DEFAULT NULL,
  `undulatorType2` varchar(45) DEFAULT NULL,
  `undulatorType3` varchar(45) DEFAULT NULL,
  `focalSpotSizeAtSample` float DEFAULT NULL,
  `focusingOptic` varchar(255) DEFAULT NULL,
  `beamDivergenceHorizontal` float DEFAULT NULL,
  `beamDivergenceVertical` float DEFAULT NULL,
  `polarisation` float DEFAULT NULL,
  `monochromatorType` varchar(255) DEFAULT NULL,
  `setupDate` datetime DEFAULT NULL,
  `synchrotronName` varchar(255) DEFAULT NULL,
  `maxExpTimePerDataCollection` double DEFAULT NULL,
  `minExposureTimePerImage` double DEFAULT NULL,
  `goniostatMaxOscillationSpeed` double DEFAULT NULL,
  `goniostatMinOscillationWidth` double DEFAULT NULL,
  `minTransmission` double DEFAULT NULL,
  `CS` float DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BeamlineStats`
--

CREATE TABLE `BeamlineStats` (
  `beamlineStatsId` int(11) UNSIGNED NOT NULL,
  `beamline` varchar(10) DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL,
  `ringCurrent` float DEFAULT NULL,
  `energy` float DEFAULT NULL,
  `gony` float DEFAULT NULL,
  `beamW` float DEFAULT NULL,
  `beamH` float DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `scanFileW` varchar(255) DEFAULT NULL,
  `scanFileH` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_automationError`
--

CREATE TABLE `BF_automationError` (
  `automationErrorId` int(10) UNSIGNED NOT NULL,
  `errorType` varchar(40) NOT NULL,
  `solution` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_automationFault`
--

CREATE TABLE `BF_automationFault` (
  `automationFaultId` int(10) UNSIGNED NOT NULL,
  `automationErrorId` int(10) UNSIGNED DEFAULT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `severity` enum('1','2','3') DEFAULT NULL,
  `stacktrace` text DEFAULT NULL,
  `resolved` tinyint(1) DEFAULT NULL,
  `faultTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_component`
--

CREATE TABLE `BF_component` (
  `componentId` int(10) UNSIGNED NOT NULL,
  `systemId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_component_beamline`
--

CREATE TABLE `BF_component_beamline` (
  `component_beamlineId` int(10) UNSIGNED NOT NULL,
  `componentId` int(10) UNSIGNED DEFAULT NULL,
  `beamlinename` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_fault`
--

CREATE TABLE `BF_fault` (
  `faultId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `subcomponentId` int(10) UNSIGNED DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `beamtimelost` tinyint(1) DEFAULT NULL,
  `beamtimelost_starttime` datetime DEFAULT NULL,
  `beamtimelost_endtime` datetime DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `resolved` tinyint(1) DEFAULT NULL,
  `resolution` text DEFAULT NULL,
  `assignee` varchar(50) DEFAULT NULL,
  `attachment` varchar(200) DEFAULT NULL,
  `eLogId` int(11) DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `assigneeId` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_subcomponent`
--

CREATE TABLE `BF_subcomponent` (
  `subcomponentId` int(10) UNSIGNED NOT NULL,
  `componentId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_subcomponent_beamline`
--

CREATE TABLE `BF_subcomponent_beamline` (
  `subcomponent_beamlineId` int(10) UNSIGNED NOT NULL,
  `subcomponentId` int(10) UNSIGNED DEFAULT NULL,
  `beamlinename` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_system`
--

CREATE TABLE `BF_system` (
  `systemId` int(10) UNSIGNED NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BF_system_beamline`
--

CREATE TABLE `BF_system_beamline` (
  `system_beamlineId` int(10) UNSIGNED NOT NULL,
  `systemId` int(10) UNSIGNED DEFAULT NULL,
  `beamlineName` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSample`
--

CREATE TABLE `BLSample` (
  `blSampleId` int(10) UNSIGNED NOT NULL,
  `diffractionPlanId` int(10) UNSIGNED DEFAULT NULL,
  `crystalId` int(10) UNSIGNED DEFAULT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `holderLength` double DEFAULT NULL,
  `loopLength` double DEFAULT NULL,
  `loopType` varchar(45) DEFAULT NULL,
  `wireWidth` double DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `completionStage` varchar(45) DEFAULT NULL,
  `structureStage` varchar(45) DEFAULT NULL,
  `publicationStage` varchar(45) DEFAULT NULL,
  `publicationComments` varchar(255) DEFAULT NULL,
  `blSampleStatus` varchar(20) DEFAULT NULL,
  `isInSampleChanger` tinyint(1) DEFAULT NULL,
  `lastKnownCenteringPosition` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time',
  `SMILES` varchar(400) DEFAULT NULL COMMENT 'the symbolic description of the structure of a chemical compound',
  `lastImageURL` varchar(255) DEFAULT NULL,
  `positionId` int(11) UNSIGNED DEFAULT NULL,
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL,
  `screenComponentGroupId` int(11) UNSIGNED DEFAULT NULL,
  `volume` float DEFAULT NULL,
  `dimension1` double DEFAULT NULL,
  `dimension2` double DEFAULT NULL,
  `dimension3` double DEFAULT NULL,
  `shape` varchar(15) DEFAULT NULL,
  `subLocation` smallint(5) UNSIGNED DEFAULT NULL COMMENT 'Indicates the sample''s location on a multi-sample pin, where 1 is closest to the pin base'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleGroup`
--

CREATE TABLE `BLSampleGroup` (
  `blSampleGroupId` int(11) UNSIGNED NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT 'Human-readable name'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleGroup_has_BLSample`
--

CREATE TABLE `BLSampleGroup_has_BLSample` (
  `blSampleGroupId` int(11) UNSIGNED NOT NULL,
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `order` mediumint(9) DEFAULT NULL,
  `type` enum('background','container','sample','calibrant') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleImage`
--

CREATE TABLE `BLSampleImage` (
  `blSampleImageId` int(11) UNSIGNED NOT NULL,
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `micronsPerPixelX` float DEFAULT NULL,
  `micronsPerPixelY` float DEFAULT NULL,
  `imageFullPath` varchar(255) DEFAULT NULL,
  `blSampleImageScoreId` int(11) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `blTimeStamp` datetime DEFAULT NULL,
  `containerInspectionId` int(11) UNSIGNED DEFAULT NULL,
  `modifiedTimeStamp` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleImageAnalysis`
--

CREATE TABLE `BLSampleImageAnalysis` (
  `blSampleImageAnalysisId` int(11) UNSIGNED NOT NULL,
  `blSampleImageId` int(11) UNSIGNED DEFAULT NULL,
  `oavSnapshotBefore` varchar(255) DEFAULT NULL,
  `oavSnapshotAfter` varchar(255) DEFAULT NULL,
  `deltaX` int(11) DEFAULT NULL,
  `deltaY` int(11) DEFAULT NULL,
  `goodnessOfFit` float DEFAULT NULL,
  `scaleFactor` float DEFAULT NULL,
  `resultCode` varchar(15) DEFAULT NULL,
  `matchStartTimeStamp` timestamp NULL DEFAULT current_timestamp(),
  `matchEndTimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleImageAutoScoreClass`
--

CREATE TABLE `BLSampleImageAutoScoreClass` (
  `blSampleImageAutoScoreClassId` tinyint(3) NOT NULL,
  `blSampleImageAutoScoreSchemaId` tinyint(3) DEFAULT NULL,
  `scoreClass` varchar(15) NOT NULL COMMENT 'Thing being scored e.g. crystal, precipitant'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='The automated scoring classes - the thing being scored';

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleImageAutoScoreSchema`
--

CREATE TABLE `BLSampleImageAutoScoreSchema` (
  `blSampleImageAutoScoreSchemaId` tinyint(3) NOT NULL,
  `schemaName` varchar(25) NOT NULL COMMENT 'Name of the schema e.g. Hampton, MARCO',
  `enabled` tinyint(1) DEFAULT 1 COMMENT 'Whether this schema is enabled (could be configurable in the UI)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Scoring schema name and whether it is enabled';

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleImageScore`
--

CREATE TABLE `BLSampleImageScore` (
  `blSampleImageScoreId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `colour` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleType`
--

CREATE TABLE `BLSampleType` (
  `blSampleTypeId` int(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `proposalType` varchar(10) DEFAULT NULL,
  `active` tinyint(1) DEFAULT 1 COMMENT '1=active, 0=inactive'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleType_has_Component`
--

CREATE TABLE `BLSampleType_has_Component` (
  `blSampleTypeId` int(10) UNSIGNED NOT NULL,
  `componentId` int(10) UNSIGNED NOT NULL,
  `abundance` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSample_has_DiffractionPlan`
--

CREATE TABLE `BLSample_has_DiffractionPlan` (
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `diffractionPlanId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSample_has_EnergyScan`
--

CREATE TABLE `BLSample_has_EnergyScan` (
  `blSampleId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `energyScanId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `blSampleHasEnergyScanId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSession`
--

CREATE TABLE `BLSession` (
  `sessionId` int(10) UNSIGNED NOT NULL,
  `expSessionPk` int(11) UNSIGNED DEFAULT NULL COMMENT 'smis session Pk ',
  `beamLineSetupId` int(10) UNSIGNED DEFAULT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `beamCalendarId` int(10) UNSIGNED DEFAULT NULL,
  `projectCode` varchar(45) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `beamLineName` varchar(45) DEFAULT NULL,
  `scheduled` tinyint(1) DEFAULT NULL,
  `nbShifts` int(10) UNSIGNED DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  `beamLineOperator` varchar(255) DEFAULT NULL,
  `visit_number` int(10) UNSIGNED DEFAULT 0,
  `bltimeStamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `usedFlag` tinyint(1) DEFAULT NULL COMMENT 'indicates if session has Datacollections or XFE or EnergyScans attached',
  `sessionTitle` varchar(255) DEFAULT NULL COMMENT 'fx accounts only',
  `structureDeterminations` float DEFAULT NULL,
  `dewarTransport` float DEFAULT NULL,
  `databackupFrance` float DEFAULT NULL COMMENT 'data backup and express delivery France',
  `databackupEurope` float DEFAULT NULL COMMENT 'data backup and express delivery Europe',
  `operatorSiteNumber` varchar(10) DEFAULT NULL COMMENT 'matricule site',
  `lastUpdate` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'last update timestamp: by default the end of the session, the last collect...',
  `protectedData` varchar(1024) DEFAULT NULL COMMENT 'indicates if the data are protected or not',
  `externalId` binary(16) DEFAULT NULL,
  `nbReimbDewars` int(10) DEFAULT NULL,
  `archived` tinyint(1) DEFAULT 0 COMMENT 'The data for the session is archived and no longer available on disk'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSession_has_SCPosition`
--

CREATE TABLE `BLSession_has_SCPosition` (
  `blsessionhasscpositionid` int(11) UNSIGNED NOT NULL,
  `blsessionid` int(11) UNSIGNED NOT NULL,
  `scContainer` smallint(5) UNSIGNED DEFAULT NULL COMMENT 'Position of container within sample changer',
  `containerPosition` smallint(5) UNSIGNED DEFAULT NULL COMMENT 'Position of sample within container'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BLSubSample`
--

CREATE TABLE `BLSubSample` (
  `blSubSampleId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `blSampleId` int(10) UNSIGNED NOT NULL COMMENT 'sample',
  `diffractionPlanId` int(10) UNSIGNED DEFAULT NULL COMMENT 'eventually diffractionPlan',
  `positionId` int(11) UNSIGNED DEFAULT NULL COMMENT 'position of the subsample',
  `position2Id` int(11) UNSIGNED DEFAULT NULL,
  `blSubSampleUUID` varchar(45) DEFAULT NULL COMMENT 'uuid of the blsubsample',
  `imgFileName` varchar(255) DEFAULT NULL COMMENT 'image filename',
  `imgFilePath` varchar(1024) DEFAULT NULL COMMENT 'url image',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time',
  `motorPositionId` int(11) UNSIGNED DEFAULT NULL COMMENT 'motor position'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Buffer`
--

CREATE TABLE `Buffer` (
  `bufferId` int(10) NOT NULL,
  `proposalId` int(10) NOT NULL DEFAULT -1,
  `safetyLevelId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `pH` varchar(45) DEFAULT NULL,
  `composition` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `BLSessionId` int(11) UNSIGNED DEFAULT NULL,
  `electronDensity` float(7,5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BufferHasAdditive`
--

CREATE TABLE `BufferHasAdditive` (
  `bufferHasAdditiveId` int(10) NOT NULL,
  `bufferId` int(10) NOT NULL,
  `additiveId` int(10) NOT NULL,
  `measurementUnitId` int(10) DEFAULT NULL,
  `quantity` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `CalendarHash`
--

CREATE TABLE `CalendarHash` (
  `calendarHashId` int(10) UNSIGNED NOT NULL,
  `ckey` varchar(50) DEFAULT NULL,
  `hash` varchar(128) DEFAULT NULL,
  `beamline` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Lets people get to their calendars without logging in using a private (hash) url';

-- --------------------------------------------------------

--
-- Table structure for table `ComponentSubType`
--

CREATE TABLE `ComponentSubType` (
  `componentSubTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL,
  `hasPh` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ComponentType`
--

CREATE TABLE `ComponentType` (
  `componentTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Component_has_SubType`
--

CREATE TABLE `Component_has_SubType` (
  `componentId` int(10) UNSIGNED NOT NULL,
  `componentSubTypeId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ConcentrationType`
--

CREATE TABLE `ConcentrationType` (
  `concentrationTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL,
  `symbol` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Container`
--

CREATE TABLE `Container` (
  `containerId` int(10) UNSIGNED NOT NULL,
  `dewarId` int(10) UNSIGNED DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `containerType` varchar(20) DEFAULT NULL,
  `capacity` int(10) UNSIGNED DEFAULT NULL,
  `beamlineLocation` varchar(20) DEFAULT NULL,
  `sampleChangerLocation` varchar(20) DEFAULT NULL,
  `containerStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` datetime DEFAULT NULL,
  `barcode` varchar(45) DEFAULT NULL,
  `sessionId` int(10) UNSIGNED DEFAULT NULL,
  `ownerId` int(10) UNSIGNED DEFAULT NULL,
  `screenId` int(11) UNSIGNED DEFAULT NULL,
  `scheduleId` int(11) UNSIGNED DEFAULT NULL,
  `imagerId` int(11) UNSIGNED DEFAULT NULL,
  `scLocationUpdated` datetime DEFAULT NULL,
  `requestedImagerId` int(11) UNSIGNED DEFAULT NULL,
  `requestedReturn` tinyint(1) DEFAULT 0 COMMENT 'True for requesting return, False means container will be disposed',
  `comments` varchar(255) DEFAULT NULL,
  `experimentType` varchar(20) DEFAULT NULL,
  `storageTemperature` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerHistory`
--

CREATE TABLE `ContainerHistory` (
  `containerHistoryId` int(11) UNSIGNED NOT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `blTimeStamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerInspection`
--

CREATE TABLE `ContainerInspection` (
  `containerInspectionId` int(11) UNSIGNED NOT NULL,
  `containerId` int(11) UNSIGNED NOT NULL,
  `inspectionTypeId` int(11) UNSIGNED NOT NULL,
  `imagerId` int(11) UNSIGNED DEFAULT NULL,
  `temperature` float DEFAULT NULL,
  `blTimeStamp` datetime DEFAULT NULL,
  `scheduleComponentid` int(11) UNSIGNED DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `priority` smallint(6) DEFAULT NULL,
  `manual` tinyint(1) DEFAULT NULL,
  `scheduledTimeStamp` datetime DEFAULT NULL,
  `completedTimeStamp` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerQueue`
--

CREATE TABLE `ContainerQueue` (
  `containerQueueId` int(11) UNSIGNED NOT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `completedTimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerQueueSample`
--

CREATE TABLE `ContainerQueueSample` (
  `containerQueueSampleId` int(11) UNSIGNED NOT NULL,
  `containerQueueId` int(11) UNSIGNED DEFAULT NULL,
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerRegistry`
--

CREATE TABLE `ContainerRegistry` (
  `containerRegistryId` int(11) NOT NULL,
  `barcode` varchar(20) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `recordTimestamp` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerType`
--

CREATE TABLE `ContainerType` (
  `containerTypeId` int(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `proposalType` varchar(10) DEFAULT NULL,
  `active` tinyint(1) DEFAULT 1 COMMENT '1=active, 0=inactive',
  `capacity` int(11) DEFAULT NULL,
  `wellPerRow` smallint(6) DEFAULT NULL,
  `dropPerWellX` smallint(6) DEFAULT NULL,
  `dropPerWellY` smallint(6) DEFAULT NULL,
  `dropHeight` float DEFAULT NULL,
  `dropWidth` float DEFAULT NULL,
  `dropOffsetX` float DEFAULT NULL,
  `dropOffsetY` float DEFAULT NULL,
  `wellDrop` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='A lookup table for different types of containers';

-- --------------------------------------------------------

--
-- Table structure for table `CryoemInitialModel`
--

CREATE TABLE `CryoemInitialModel` (
  `cryoemInitialModelId` int(10) UNSIGNED NOT NULL,
  `resolution` float DEFAULT NULL COMMENT 'Unit: Angstroms',
  `numberOfParticles` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Initial cryo-EM model generation results';

-- --------------------------------------------------------

--
-- Table structure for table `Crystal`
--

CREATE TABLE `Crystal` (
  `crystalId` int(10) UNSIGNED NOT NULL,
  `diffractionPlanId` int(10) UNSIGNED DEFAULT NULL,
  `proteinId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `crystalUUID` varchar(45) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `spaceGroup` varchar(20) DEFAULT NULL,
  `morphology` varchar(255) DEFAULT NULL,
  `color` varchar(45) DEFAULT NULL,
  `size_X` double DEFAULT NULL,
  `size_Y` double DEFAULT NULL,
  `size_Z` double DEFAULT NULL,
  `cell_a` double DEFAULT NULL,
  `cell_b` double DEFAULT NULL,
  `cell_c` double DEFAULT NULL,
  `cell_alpha` double DEFAULT NULL,
  `cell_beta` double DEFAULT NULL,
  `cell_gamma` double DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `pdbFileName` varchar(255) DEFAULT NULL COMMENT 'pdb file name',
  `pdbFilePath` varchar(1024) DEFAULT NULL COMMENT 'pdb file path',
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time',
  `abundance` float DEFAULT NULL,
  `packingFraction` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Crystal_has_UUID`
--

CREATE TABLE `Crystal_has_UUID` (
  `crystal_has_UUID_Id` int(10) UNSIGNED NOT NULL,
  `crystalId` int(10) UNSIGNED NOT NULL,
  `UUID` varchar(45) DEFAULT NULL,
  `imageURL` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `CTF`
--

CREATE TABLE `CTF` (
  `CTFid` int(11) NOT NULL,
  `motionCorrectionId` int(11) NOT NULL,
  `spectraImageThumbnailFullPath` varchar(512) DEFAULT NULL,
  `spectraImageFullPath` varchar(512) DEFAULT NULL,
  `defocusU` varchar(45) DEFAULT NULL,
  `defocusV` varchar(45) DEFAULT NULL,
  `angle` varchar(45) DEFAULT NULL,
  `crossCorrelationCoefficient` varchar(45) DEFAULT NULL,
  `resolutionLimit` varchar(45) DEFAULT NULL,
  `estimatedBfactor` varchar(45) DEFAULT NULL,
  `logFilePath` varchar(512) DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DataAcquisition`
--

CREATE TABLE `DataAcquisition` (
  `dataAcquisitionId` int(10) NOT NULL,
  `sampleCellId` int(10) NOT NULL,
  `framesCount` varchar(45) DEFAULT NULL,
  `energy` varchar(45) DEFAULT NULL,
  `waitTime` varchar(45) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollection`
--

CREATE TABLE `DataCollection` (
  `dataCollectionId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `dataCollectionGroupId` int(11) NOT NULL COMMENT 'references DataCollectionGroup table',
  `strategySubWedgeOrigId` int(10) UNSIGNED DEFAULT NULL COMMENT 'references ScreeningStrategySubWedge table',
  `detectorId` int(11) DEFAULT NULL COMMENT 'references Detector table',
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL,
  `startPositionId` int(11) UNSIGNED DEFAULT NULL,
  `endPositionId` int(11) UNSIGNED DEFAULT NULL,
  `dataCollectionNumber` int(10) UNSIGNED DEFAULT NULL,
  `startTime` datetime DEFAULT NULL COMMENT 'Start time of the dataCollection',
  `endTime` datetime DEFAULT NULL COMMENT 'end time of the dataCollection',
  `runStatus` varchar(45) DEFAULT NULL,
  `axisStart` float DEFAULT NULL,
  `axisEnd` float DEFAULT NULL,
  `axisRange` float DEFAULT NULL,
  `overlap` float DEFAULT NULL,
  `numberOfImages` int(10) UNSIGNED DEFAULT NULL,
  `startImageNumber` int(10) UNSIGNED DEFAULT NULL,
  `numberOfPasses` int(10) UNSIGNED DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `imageDirectory` varchar(255) DEFAULT NULL,
  `imagePrefix` varchar(100) DEFAULT NULL,
  `imageSuffix` varchar(45) DEFAULT NULL,
  `imageContainerSubPath` varchar(255) DEFAULT NULL COMMENT 'Internal path of a HDF5 file pointing to the data for this data collection',
  `fileTemplate` varchar(255) DEFAULT NULL,
  `wavelength` float DEFAULT NULL,
  `resolution` float DEFAULT NULL,
  `detectorDistance` float DEFAULT NULL,
  `xBeam` float DEFAULT NULL,
  `yBeam` float DEFAULT NULL,
  `xBeamPix` float DEFAULT NULL COMMENT 'Beam size in pixels',
  `yBeamPix` float DEFAULT NULL COMMENT 'Beam size in pixels',
  `comments` varchar(1024) DEFAULT NULL,
  `printableForReport` tinyint(1) UNSIGNED DEFAULT 1,
  `slitGapVertical` float DEFAULT NULL,
  `slitGapHorizontal` float DEFAULT NULL,
  `transmission` float DEFAULT NULL,
  `synchrotronMode` varchar(20) DEFAULT NULL,
  `xtalSnapshotFullPath1` varchar(255) DEFAULT NULL,
  `xtalSnapshotFullPath2` varchar(255) DEFAULT NULL,
  `xtalSnapshotFullPath3` varchar(255) DEFAULT NULL,
  `xtalSnapshotFullPath4` varchar(255) DEFAULT NULL,
  `rotationAxis` enum('Omega','Kappa','Phi') DEFAULT NULL,
  `phiStart` float DEFAULT NULL,
  `kappaStart` float DEFAULT NULL,
  `omegaStart` float DEFAULT NULL,
  `resolutionAtCorner` float DEFAULT NULL,
  `detector2Theta` float DEFAULT NULL,
  `undulatorGap1` float DEFAULT NULL,
  `undulatorGap2` float DEFAULT NULL,
  `undulatorGap3` float DEFAULT NULL,
  `beamSizeAtSampleX` float DEFAULT NULL,
  `beamSizeAtSampleY` float DEFAULT NULL,
  `centeringMethod` varchar(255) DEFAULT NULL,
  `averageTemperature` float DEFAULT NULL,
  `actualCenteringPosition` varchar(255) DEFAULT NULL,
  `beamShape` varchar(45) DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `flux_end` double DEFAULT NULL COMMENT 'flux measured after the collect',
  `totalAbsorbedDose` double DEFAULT NULL COMMENT 'expected dose delivered to the crystal, EDNA',
  `bestWilsonPlotPath` varchar(255) DEFAULT NULL,
  `imageQualityIndicatorsPlotPath` varchar(512) DEFAULT NULL,
  `imageQualityIndicatorsCSVPath` varchar(512) DEFAULT NULL,
  `blSampleId` int(11) UNSIGNED DEFAULT NULL,
  `sessionId` int(11) UNSIGNED DEFAULT 0,
  `experimentType` varchar(24) DEFAULT NULL,
  `crystalClass` varchar(20) DEFAULT NULL,
  `chiStart` float DEFAULT NULL,
  `detectorMode` varchar(255) DEFAULT NULL,
  `actualSampleBarcode` varchar(45) DEFAULT NULL,
  `actualSampleSlotInContainer` int(11) UNSIGNED DEFAULT NULL,
  `actualContainerBarcode` varchar(45) DEFAULT NULL,
  `actualContainerSlotInSC` int(11) UNSIGNED DEFAULT NULL,
  `positionId` int(11) UNSIGNED DEFAULT NULL,
  `focalSpotSizeAtSampleX` float DEFAULT NULL,
  `polarisation` float DEFAULT NULL,
  `focalSpotSizeAtSampleY` float DEFAULT NULL,
  `apertureId` int(11) UNSIGNED DEFAULT NULL,
  `screeningOrigId` int(11) UNSIGNED DEFAULT NULL,
  `processedDataFile` varchar(255) DEFAULT NULL,
  `datFullPath` varchar(255) DEFAULT NULL,
  `magnification` int(11) DEFAULT NULL COMMENT 'Unit: X',
  `binning` tinyint(1) DEFAULT 1 COMMENT '1 or 2. Number of pixels to process as 1. (Use mean value.)',
  `particleDiameter` float DEFAULT NULL COMMENT 'Unit: nm',
  `boxSize_CTF` float DEFAULT NULL COMMENT 'Unit: pixels',
  `minResolution` float DEFAULT NULL COMMENT 'Unit: A',
  `minDefocus` float DEFAULT NULL COMMENT 'Unit: A',
  `maxDefocus` float DEFAULT NULL COMMENT 'Unit: A',
  `defocusStepSize` float DEFAULT NULL COMMENT 'Unit: A',
  `amountAstigmatism` float DEFAULT NULL COMMENT 'Unit: A',
  `extractSize` float DEFAULT NULL COMMENT 'Unit: pixels',
  `bgRadius` float DEFAULT NULL COMMENT 'Unit: nm',
  `voltage` float DEFAULT NULL COMMENT 'Unit: kV',
  `objAperture` float DEFAULT NULL COMMENT 'Unit: um',
  `c1aperture` float DEFAULT NULL COMMENT 'Unit: um',
  `c2aperture` float DEFAULT NULL COMMENT 'Unit: um',
  `c3aperture` float DEFAULT NULL COMMENT 'Unit: um',
  `c1lens` float DEFAULT NULL COMMENT 'Unit: %',
  `c2lens` float DEFAULT NULL COMMENT 'Unit: %',
  `c3lens` float DEFAULT NULL COMMENT 'Unit: %'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollectionFileAttachment`
--

CREATE TABLE `DataCollectionFileAttachment` (
  `dataCollectionFileAttachmentId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `fileFullPath` varchar(255) NOT NULL,
  `fileType` enum('snapshot','log','xy','recip') DEFAULT NULL COMMENT 'snapshot: image file, usually of the sample. \r\nlog: a text file with logging info. \r\nxy: x and y data in text format. \r\nrecip: a compressed csv file with reciprocal space coordinates.',
  `createTime` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollectionGroup`
--

CREATE TABLE `DataCollectionGroup` (
  `dataCollectionGroupId` int(11) NOT NULL COMMENT 'Primary key (auto-incremented)',
  `blSampleId` int(10) UNSIGNED DEFAULT NULL COMMENT 'references BLSample table',
  `sessionId` int(10) UNSIGNED NOT NULL COMMENT 'references Session table',
  `workflowId` int(11) UNSIGNED DEFAULT NULL,
  `experimentType` enum('EM','SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration','Still') DEFAULT NULL COMMENT 'Experiment type flag',
  `startTime` datetime DEFAULT NULL COMMENT 'Start time of the dataCollectionGroup',
  `endTime` datetime DEFAULT NULL COMMENT 'end time of the dataCollectionGroup',
  `crystalClass` varchar(20) DEFAULT NULL COMMENT 'Crystal Class for industrials users',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `detectorMode` varchar(255) DEFAULT NULL COMMENT 'Detector mode',
  `actualSampleBarcode` varchar(45) DEFAULT NULL COMMENT 'Actual sample barcode',
  `actualSampleSlotInContainer` int(10) UNSIGNED DEFAULT NULL COMMENT 'Actual sample slot number in container',
  `actualContainerBarcode` varchar(45) DEFAULT NULL COMMENT 'Actual container barcode',
  `actualContainerSlotInSC` int(10) UNSIGNED DEFAULT NULL COMMENT 'Actual container slot number in sample changer',
  `xtalSnapshotFullPath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='a dataCollectionGroup is a group of dataCollection for a spe';

-- --------------------------------------------------------

--
-- Table structure for table `DataCollectionPlanGroup`
--

CREATE TABLE `DataCollectionPlanGroup` (
  `dataCollectionPlanGroupId` int(11) UNSIGNED NOT NULL,
  `sessionId` int(11) UNSIGNED DEFAULT NULL,
  `blSampleId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DatamatrixInSampleChanger`
--

CREATE TABLE `DatamatrixInSampleChanger` (
  `datamatrixInSampleChangerId` int(10) UNSIGNED NOT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `beamLineName` varchar(45) DEFAULT NULL,
  `datamatrixCode` varchar(45) DEFAULT NULL,
  `locationInContainer` int(11) DEFAULT NULL,
  `containerLocationInSC` int(11) DEFAULT NULL,
  `containerDatamatrixCode` varchar(45) DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DataReductionStatus`
--

CREATE TABLE `DataReductionStatus` (
  `dataReductionStatusId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `status` varchar(15) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Detector`
--

CREATE TABLE `Detector` (
  `detectorId` int(11) NOT NULL COMMENT 'Primary key (auto-incremented)',
  `detectorType` varchar(255) DEFAULT NULL,
  `detectorManufacturer` varchar(255) DEFAULT NULL,
  `detectorModel` varchar(255) DEFAULT NULL,
  `detectorPixelSizeHorizontal` float DEFAULT NULL,
  `detectorPixelSizeVertical` float DEFAULT NULL,
  `detectorSerialNumber` varchar(30) DEFAULT NULL,
  `detectorDistanceMin` double DEFAULT NULL,
  `detectorDistanceMax` double DEFAULT NULL,
  `trustedPixelValueRangeLower` double DEFAULT NULL,
  `trustedPixelValueRangeUpper` double DEFAULT NULL,
  `sensorThickness` float DEFAULT NULL,
  `overload` float DEFAULT NULL,
  `XGeoCorr` varchar(255) DEFAULT NULL,
  `YGeoCorr` varchar(255) DEFAULT NULL,
  `detectorMode` varchar(255) DEFAULT NULL,
  `detectorMaxResolution` float DEFAULT NULL,
  `detectorMinResolution` float DEFAULT NULL,
  `CS` float DEFAULT NULL COMMENT 'Unit: mm',
  `density` float DEFAULT NULL,
  `composition` varchar(16) DEFAULT NULL,
  `localName` varchar(40) DEFAULT NULL COMMENT 'Colloquial name for the detector'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Detector table is linked to a dataCollection';

-- --------------------------------------------------------

--
-- Table structure for table `Dewar`
--

CREATE TABLE `Dewar` (
  `dewarId` int(10) UNSIGNED NOT NULL,
  `shippingId` int(10) UNSIGNED DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `comments` tinytext DEFAULT NULL,
  `storageLocation` varchar(45) DEFAULT NULL,
  `dewarStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT current_timestamp(),
  `isStorageDewar` tinyint(1) DEFAULT 0,
  `barCode` varchar(45) DEFAULT NULL,
  `firstExperimentId` int(10) UNSIGNED DEFAULT NULL,
  `customsValue` int(11) UNSIGNED DEFAULT NULL,
  `transportValue` int(11) UNSIGNED DEFAULT NULL,
  `trackingNumberToSynchrotron` varchar(30) DEFAULT NULL,
  `trackingNumberFromSynchrotron` varchar(30) DEFAULT NULL,
  `facilityCode` varchar(20) DEFAULT NULL COMMENT 'Unique barcode assigned to each dewar',
  `isReimbursed` tinyint(1) DEFAULT 0 COMMENT 'set this dewar as reimbursed by the user office',
  `type` enum('Dewar','Toolbox') NOT NULL DEFAULT 'Dewar'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DewarLocation`
--

CREATE TABLE `DewarLocation` (
  `eventId` int(10) UNSIGNED NOT NULL,
  `dewarNumber` varchar(128) NOT NULL COMMENT 'Dewar number',
  `userId` varchar(128) DEFAULT NULL COMMENT 'User who locates the dewar',
  `dateTime` datetime DEFAULT NULL COMMENT 'Date and time of locatization',
  `locationName` varchar(128) DEFAULT NULL COMMENT 'Location of the dewar',
  `courierName` varchar(128) DEFAULT NULL COMMENT 'Carrier name who''s shipping back the dewar',
  `courierTrackingNumber` varchar(128) DEFAULT NULL COMMENT 'Tracking number of the shippment'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='ISPyB Dewar location table';

-- --------------------------------------------------------

--
-- Table structure for table `DewarLocationList`
--

CREATE TABLE `DewarLocationList` (
  `locationId` int(10) UNSIGNED NOT NULL,
  `locationName` varchar(128) NOT NULL DEFAULT '' COMMENT 'Location'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='List of locations for dewars';

-- --------------------------------------------------------

--
-- Table structure for table `DewarRegistry`
--

CREATE TABLE `DewarRegistry` (
  `dewarRegistryId` int(11) UNSIGNED NOT NULL,
  `facilityCode` varchar(20) NOT NULL,
  `proposalId` int(11) UNSIGNED DEFAULT NULL,
  `labContactId` int(11) UNSIGNED DEFAULT NULL,
  `purchaseDate` datetime DEFAULT NULL,
  `bltimestamp` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DewarRegistry_has_Proposal`
--

CREATE TABLE `DewarRegistry_has_Proposal` (
  `dewarRegistryHasProposalId` int(11) UNSIGNED NOT NULL,
  `dewarRegistryId` int(11) UNSIGNED DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Person registering the dewar',
  `recordTimestamp` datetime DEFAULT current_timestamp(),
  `labContactId` int(11) UNSIGNED DEFAULT NULL COMMENT 'Owner of the dewar'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DewarTransportHistory`
--

CREATE TABLE `DewarTransportHistory` (
  `DewarTransportHistoryId` int(10) UNSIGNED NOT NULL,
  `dewarId` int(10) UNSIGNED DEFAULT NULL,
  `dewarStatus` varchar(45) NOT NULL,
  `storageLocation` varchar(45) DEFAULT NULL,
  `arrivalDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DiffractionPlan`
--

CREATE TABLE `DiffractionPlan` (
  `diffractionPlanId` int(10) UNSIGNED NOT NULL,
  `xmlDocumentId` int(10) UNSIGNED DEFAULT NULL,
  `experimentKind` enum('Default','MXPressE','MXPressF','MXPressO','MXPressP','MXPressP_SAD','MXPressI','MXPressE_SAD','MXScore','MXPressM','MAD','SAD','Fixed','Ligand binding','Refinement','OSC','MAD - Inverse Beam','SAD - Inverse Beam') DEFAULT NULL,
  `observedResolution` float DEFAULT NULL,
  `minimalResolution` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `oscillationRange` float DEFAULT NULL,
  `maximalResolution` float DEFAULT NULL,
  `screeningResolution` float DEFAULT NULL,
  `radiationSensitivity` float DEFAULT NULL,
  `anomalousScatterer` varchar(255) DEFAULT NULL,
  `preferredBeamSizeX` float DEFAULT NULL,
  `preferredBeamSizeY` float DEFAULT NULL,
  `preferredBeamDiameter` float DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `aimedCompleteness` double DEFAULT NULL,
  `aimedIOverSigmaAtHighestRes` double DEFAULT NULL,
  `aimedMultiplicity` double DEFAULT NULL,
  `aimedResolution` double DEFAULT NULL,
  `anomalousData` tinyint(1) DEFAULT 0,
  `complexity` varchar(45) DEFAULT NULL,
  `estimateRadiationDamage` tinyint(1) DEFAULT 0,
  `forcedSpaceGroup` varchar(45) DEFAULT NULL,
  `requiredCompleteness` double DEFAULT NULL,
  `requiredMultiplicity` double DEFAULT NULL,
  `requiredResolution` double DEFAULT NULL,
  `strategyOption` varchar(45) DEFAULT NULL,
  `kappaStrategyOption` varchar(45) DEFAULT NULL,
  `numberOfPositions` int(11) DEFAULT NULL,
  `minDimAccrossSpindleAxis` double DEFAULT NULL COMMENT 'minimum dimension accross the spindle axis',
  `maxDimAccrossSpindleAxis` double DEFAULT NULL COMMENT 'maximum dimension accross the spindle axis',
  `radiationSensitivityBeta` double DEFAULT NULL,
  `radiationSensitivityGamma` double DEFAULT NULL,
  `minOscWidth` float DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time',
  `diffractionPlanUUID` varchar(1000) DEFAULT NULL,
  `dataCollectionPlanGroupId` int(11) UNSIGNED DEFAULT NULL,
  `detectorId` int(11) DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `orientation` double DEFAULT NULL,
  `monoBandwidth` double DEFAULT NULL,
  `monochromator` varchar(8) DEFAULT NULL COMMENT 'DMM or DCM',
  `energy` float DEFAULT NULL COMMENT 'eV',
  `transmission` float DEFAULT NULL COMMENT 'Decimal fraction in range [0,1]',
  `boxSizeX` float DEFAULT NULL COMMENT 'microns',
  `boxSizeY` float DEFAULT NULL COMMENT 'microns',
  `kappaStart` float DEFAULT NULL COMMENT 'degrees',
  `axisStart` float DEFAULT NULL COMMENT 'degrees',
  `axisRange` float DEFAULT NULL COMMENT 'degrees',
  `numberOfImages` mediumint(9) DEFAULT NULL COMMENT 'The number of images requested',
  `presetForProposalId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Indicates this plan is available to all sessions on given proposal',
  `beamLineName` varchar(45) DEFAULT NULL COMMENT 'Indicates this plan is available to all sessions on given beamline',
  `userPath` varchar(100) DEFAULT NULL COMMENT 'User-specified relative "root" path inside the session directory to be used for holding collected data'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DiffractionPlan_has_Detector`
--

CREATE TABLE `DiffractionPlan_has_Detector` (
  `diffractionPlanId` int(11) UNSIGNED NOT NULL,
  `detectorId` int(11) NOT NULL,
  `exposureTime` double DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `orientation` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `EMMicroscope`
--

CREATE TABLE `EMMicroscope` (
  `emMicroscopeId` int(11) UNSIGNED NOT NULL,
  `instrumentName` varchar(100) NOT NULL,
  `voltage` float DEFAULT NULL,
  `CS` float DEFAULT NULL COMMENT 'Unit: mm',
  `detectorPixelSize` float DEFAULT NULL,
  `C2aperture` float DEFAULT NULL,
  `ObjAperture` float DEFAULT NULL,
  `C2lens` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `EnergyScan`
--

CREATE TABLE `EnergyScan` (
  `energyScanId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `blSampleId` int(10) UNSIGNED DEFAULT NULL,
  `fluorescenceDetector` varchar(255) DEFAULT NULL,
  `scanFileFullPath` varchar(255) DEFAULT NULL,
  `choochFileFullPath` varchar(255) DEFAULT NULL,
  `jpegChoochFileFullPath` varchar(255) DEFAULT NULL,
  `element` varchar(45) DEFAULT NULL,
  `startEnergy` float DEFAULT NULL,
  `endEnergy` float DEFAULT NULL,
  `transmissionFactor` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `axisPosition` float DEFAULT NULL,
  `synchrotronCurrent` float DEFAULT NULL,
  `temperature` float DEFAULT NULL,
  `peakEnergy` float DEFAULT NULL,
  `peakFPrime` float DEFAULT NULL,
  `peakFDoublePrime` float DEFAULT NULL,
  `inflectionEnergy` float DEFAULT NULL,
  `inflectionFPrime` float DEFAULT NULL,
  `inflectionFDoublePrime` float DEFAULT NULL,
  `xrayDose` float DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `edgeEnergy` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `beamSizeVertical` float DEFAULT NULL,
  `beamSizeHorizontal` float DEFAULT NULL,
  `crystalClass` varchar(20) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `flux` double DEFAULT NULL COMMENT 'flux measured before the energyScan',
  `flux_end` double DEFAULT NULL COMMENT 'flux measured after the energyScan',
  `workingDirectory` varchar(45) DEFAULT NULL,
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL,
  `remoteEnergy` float DEFAULT NULL,
  `remoteFPrime` float DEFAULT NULL,
  `remoteFDoublePrime` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Experiment`
--

CREATE TABLE `Experiment` (
  `experimentId` int(11) NOT NULL,
  `sessionId` int(10) UNSIGNED DEFAULT NULL,
  `proposalId` int(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `experimentType` varchar(128) DEFAULT NULL,
  `sourceFilePath` varchar(256) DEFAULT NULL,
  `dataAcquisitionFilePath` varchar(256) DEFAULT NULL COMMENT 'The file path pointing to the data acquisition. Eventually it may be a compressed file with all the files or just the folder',
  `status` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ExperimentKindDetails`
--

CREATE TABLE `ExperimentKindDetails` (
  `experimentKindId` int(10) UNSIGNED NOT NULL,
  `diffractionPlanId` int(10) UNSIGNED NOT NULL,
  `exposureIndex` int(10) UNSIGNED DEFAULT NULL,
  `dataCollectionType` varchar(45) DEFAULT NULL,
  `dataCollectionKind` varchar(45) DEFAULT NULL,
  `wedgeValue` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ExperimentType`
--

CREATE TABLE `ExperimentType` (
  `experimentTypeId` int(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `proposalType` varchar(10) DEFAULT NULL,
  `active` tinyint(1) DEFAULT 1 COMMENT '1=active, 0=inactive'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='A lookup table for different types of experients';

-- --------------------------------------------------------

--
-- Table structure for table `FitStructureToExperimentalData`
--

CREATE TABLE `FitStructureToExperimentalData` (
  `fitStructureToExperimentalDataId` int(11) NOT NULL,
  `structureId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL,
  `workflowId` int(10) UNSIGNED DEFAULT NULL,
  `fitFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `outputFilePath` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Frame`
--

CREATE TABLE `Frame` (
  `frameId` int(10) NOT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `creationDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `frameSetId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `FrameList`
--

CREATE TABLE `FrameList` (
  `frameListId` int(10) NOT NULL,
  `comments` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `FrameSet`
--

CREATE TABLE `FrameSet` (
  `frameSetId` int(10) NOT NULL,
  `runId` int(10) NOT NULL,
  `frameListId` int(10) DEFAULT NULL,
  `detectorId` int(10) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `internalPath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `FrameToList`
--

CREATE TABLE `FrameToList` (
  `frameToListId` int(10) NOT NULL,
  `frameListId` int(10) NOT NULL,
  `frameId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `GeometryClassname`
--

CREATE TABLE `GeometryClassname` (
  `geometryClassnameId` int(11) UNSIGNED NOT NULL,
  `geometryClassname` varchar(45) DEFAULT NULL,
  `geometryOrder` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `GridInfo`
--

CREATE TABLE `GridInfo` (
  `gridInfoId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `workflowMeshId` int(11) UNSIGNED DEFAULT NULL,
  `xOffset` double DEFAULT NULL,
  `yOffset` double DEFAULT NULL,
  `dx_mm` double DEFAULT NULL,
  `dy_mm` double DEFAULT NULL,
  `steps_x` double DEFAULT NULL,
  `steps_y` double DEFAULT NULL,
  `meshAngle` double DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time',
  `orientation` enum('vertical','horizontal') DEFAULT 'horizontal',
  `dataCollectionGroupId` int(11) DEFAULT NULL,
  `pixelspermicronX` float DEFAULT NULL,
  `pixelspermicronY` float DEFAULT NULL,
  `snapshot_offsetxpixel` float DEFAULT NULL,
  `snapshot_offsetypixel` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Image`
--

CREATE TABLE `Image` (
  `imageId` int(12) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `motorPositionId` int(11) UNSIGNED DEFAULT NULL,
  `imageNumber` int(10) UNSIGNED DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `fileLocation` varchar(255) DEFAULT NULL,
  `measuredIntensity` float DEFAULT NULL,
  `jpegFileFullPath` varchar(255) DEFAULT NULL,
  `jpegThumbnailFileFullPath` varchar(255) DEFAULT NULL,
  `temperature` float DEFAULT NULL,
  `cumulativeIntensity` float DEFAULT NULL,
  `synchrotronCurrent` float DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `machineMessage` varchar(1024) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ImageQualityIndicators`
--

CREATE TABLE `ImageQualityIndicators` (
  `imageQualityIndicatorsId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `imageId` int(12) DEFAULT NULL,
  `autoProcProgramId` int(10) UNSIGNED NOT NULL COMMENT 'Foreign key to the AutoProcProgram table',
  `spotTotal` int(10) DEFAULT NULL COMMENT 'Total number of spots',
  `inResTotal` int(10) DEFAULT NULL COMMENT 'Total number of spots in resolution range',
  `goodBraggCandidates` int(10) DEFAULT NULL COMMENT 'Total number of Bragg diffraction spots',
  `iceRings` int(10) DEFAULT NULL COMMENT 'Number of ice rings identified',
  `method1Res` float DEFAULT NULL COMMENT 'Resolution estimate 1 (see publication)',
  `method2Res` float DEFAULT NULL COMMENT 'Resolution estimate 2 (see publication)',
  `maxUnitCell` float DEFAULT NULL COMMENT 'Estimation of the largest possible unit cell edge',
  `pctSaturationTop50Peaks` float DEFAULT NULL COMMENT 'The fraction of the dynamic range being used',
  `inResolutionOvrlSpots` int(10) DEFAULT NULL COMMENT 'Number of spots overloaded',
  `binPopCutOffMethod2Res` float DEFAULT NULL COMMENT 'Cut off used in resolution limit calculation',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `totalIntegratedSignal` double DEFAULT NULL,
  `dozor_score` double DEFAULT NULL COMMENT 'dozor_score',
  `dataCollectionId` int(11) UNSIGNED DEFAULT NULL,
  `imageNumber` mediumint(8) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Imager`
--

CREATE TABLE `Imager` (
  `imagerId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) NOT NULL,
  `temperature` float DEFAULT NULL,
  `serial` varchar(45) DEFAULT NULL,
  `capacity` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `InputParameterWorkflow`
--

CREATE TABLE `InputParameterWorkflow` (
  `inputParameterId` int(10) NOT NULL,
  `workflowId` int(10) NOT NULL,
  `parameterType` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `InspectionType`
--

CREATE TABLE `InspectionType` (
  `inspectionTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Instruction`
--

CREATE TABLE `Instruction` (
  `instructionId` int(10) NOT NULL,
  `instructionSetId` int(10) NOT NULL,
  `order` int(11) NOT NULL,
  `comments` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `InstructionSet`
--

CREATE TABLE `InstructionSet` (
  `instructionSetId` int(10) NOT NULL,
  `type` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `IspybAutoProcAttachment`
--

CREATE TABLE `IspybAutoProcAttachment` (
  `autoProcAttachmentId` int(11) NOT NULL,
  `fileName` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `step` enum('XDS','XSCALE','SCALA','SCALEPACK','TRUNCATE','DIMPLE') DEFAULT 'XDS' COMMENT 'step where the file is generated',
  `fileCategory` enum('input','output','log','correction') DEFAULT 'output',
  `hasGraph` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='ISPyB autoProcAttachment files values';

-- --------------------------------------------------------

--
-- Table structure for table `IspybCrystalClass`
--

CREATE TABLE `IspybCrystalClass` (
  `crystalClassId` int(11) NOT NULL,
  `crystalClass_code` varchar(20) NOT NULL,
  `crystalClass_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='ISPyB crystal class values';

-- --------------------------------------------------------

--
-- Table structure for table `IspybReference`
--

CREATE TABLE `IspybReference` (
  `referenceId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `referenceName` varchar(255) DEFAULT NULL COMMENT 'reference name',
  `referenceUrl` varchar(1024) DEFAULT NULL COMMENT 'url of the reference',
  `referenceBibtext` blob DEFAULT NULL COMMENT 'bibtext value of the reference',
  `beamline` enum('All','ID14-4','ID23-1','ID23-2','ID29','ID30A-1','ID30A-2','XRF','AllXRF','Mesh') DEFAULT NULL COMMENT 'beamline involved'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `LabContact`
--

CREATE TABLE `LabContact` (
  `labContactId` int(10) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL,
  `cardName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL,
  `defaultCourrierCompany` varchar(45) DEFAULT NULL,
  `courierAccount` varchar(45) DEFAULT NULL,
  `billingReference` varchar(45) DEFAULT NULL,
  `dewarAvgCustomsValue` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `dewarAvgTransportValue` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Laboratory`
--

CREATE TABLE `Laboratory` (
  `laboratoryId` int(10) UNSIGNED NOT NULL,
  `laboratoryUUID` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `organization` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time',
  `laboratoryExtPk` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Log4Stat`
--

CREATE TABLE `Log4Stat` (
  `id` int(11) NOT NULL,
  `priority` varchar(15) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Login`
--

CREATE TABLE `Login` (
  `loginId` int(11) NOT NULL,
  `token` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `roles` varchar(1024) NOT NULL,
  `siteId` varchar(45) DEFAULT NULL,
  `authorized` varchar(1024) DEFAULT NULL,
  `expirationTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Macromolecule`
--

CREATE TABLE `Macromolecule` (
  `macromoleculeId` int(11) NOT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `safetyLevelId` int(10) DEFAULT NULL,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `acronym` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extintionCoefficient` varchar(45) DEFAULT NULL,
  `molecularMass` varchar(45) DEFAULT NULL,
  `sequence` varchar(1000) DEFAULT NULL,
  `contactsDescriptionFilePath` varchar(255) DEFAULT NULL,
  `symmetry` varchar(45) DEFAULT NULL,
  `comments` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `refractiveIndex` varchar(45) DEFAULT NULL,
  `solventViscosity` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `electronDensity` float(7,5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MacromoleculeRegion`
--

CREATE TABLE `MacromoleculeRegion` (
  `macromoleculeRegionId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  `regionType` varchar(45) DEFAULT NULL,
  `id` varchar(45) DEFAULT NULL,
  `count` varchar(45) DEFAULT NULL,
  `sequence` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Measurement`
--

CREATE TABLE `Measurement` (
  `measurementId` int(10) NOT NULL,
  `specimenId` int(10) NOT NULL,
  `runId` int(10) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `imageDirectory` varchar(512) DEFAULT NULL,
  `priorityLevelId` int(10) DEFAULT NULL,
  `exposureTemperature` varchar(45) DEFAULT NULL,
  `viscosity` varchar(45) DEFAULT NULL,
  `flow` tinyint(1) DEFAULT NULL,
  `extraFlowTime` varchar(45) DEFAULT NULL,
  `volumeToLoad` varchar(45) DEFAULT NULL,
  `waitTime` varchar(45) DEFAULT NULL,
  `transmission` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `pathToH5` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MeasurementToDataCollection`
--

CREATE TABLE `MeasurementToDataCollection` (
  `measurementToDataCollectionId` int(10) NOT NULL,
  `dataCollectionId` int(10) DEFAULT NULL,
  `measurementId` int(10) DEFAULT NULL,
  `dataCollectionOrder` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MeasurementUnit`
--

CREATE TABLE `MeasurementUnit` (
  `measurementUnitId` int(10) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `unitType` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Merge`
--

CREATE TABLE `Merge` (
  `mergeId` int(10) NOT NULL,
  `measurementId` int(10) DEFAULT NULL,
  `frameListId` int(10) DEFAULT NULL,
  `discardedFrameNameList` varchar(1024) DEFAULT NULL,
  `averageFilePath` varchar(255) DEFAULT NULL,
  `framesCount` varchar(45) DEFAULT NULL,
  `framesMerge` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MixtureToStructure`
--

CREATE TABLE `MixtureToStructure` (
  `fitToStructureId` int(11) NOT NULL,
  `structureId` int(10) NOT NULL,
  `mixtureId` int(10) NOT NULL,
  `volumeFraction` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Model`
--

CREATE TABLE `Model` (
  `modelId` int(10) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `pdbFile` varchar(255) DEFAULT NULL,
  `fitFile` varchar(255) DEFAULT NULL,
  `firFile` varchar(255) DEFAULT NULL,
  `logFile` varchar(255) DEFAULT NULL,
  `rFactor` varchar(45) DEFAULT NULL,
  `chiSqrt` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `rg` varchar(45) DEFAULT NULL,
  `dMax` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ModelBuilding`
--

CREATE TABLE `ModelBuilding` (
  `modelBuildingId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related spaceGroup',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ModelList`
--

CREATE TABLE `ModelList` (
  `modelListId` int(10) NOT NULL,
  `nsdFilePath` varchar(255) DEFAULT NULL,
  `chi2RgFilePath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ModelToList`
--

CREATE TABLE `ModelToList` (
  `modelToListId` int(10) NOT NULL,
  `modelId` int(10) NOT NULL,
  `modelListId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MotionCorrection`
--

CREATE TABLE `MotionCorrection` (
  `motionCorrectionId` int(11) NOT NULL,
  `movieId` int(11) DEFAULT NULL,
  `firstFrame` varchar(45) DEFAULT NULL,
  `lastFrame` varchar(45) DEFAULT NULL,
  `dosePerFrame` varchar(45) DEFAULT NULL,
  `doseWeight` varchar(45) DEFAULT NULL,
  `totalMotion` varchar(45) DEFAULT NULL,
  `averageMotionPerFrame` varchar(45) DEFAULT NULL,
  `driftPlotFullPath` varchar(512) DEFAULT NULL,
  `micrographFullPath` varchar(512) DEFAULT NULL,
  `micrographSnapshotFullPath` varchar(512) DEFAULT NULL,
  `correctedDoseMicrographFullPath` varchar(512) DEFAULT NULL,
  `patchesUsed` varchar(45) DEFAULT NULL,
  `logFileFullPath` varchar(512) DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MotorPosition`
--

CREATE TABLE `MotorPosition` (
  `motorPositionId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phiX` double DEFAULT NULL,
  `phiY` double DEFAULT NULL,
  `phiZ` double DEFAULT NULL,
  `sampX` double DEFAULT NULL,
  `sampY` double DEFAULT NULL,
  `omega` double DEFAULT NULL,
  `kappa` double DEFAULT NULL,
  `phi` double DEFAULT NULL,
  `chi` double DEFAULT NULL,
  `gridIndexY` int(11) DEFAULT NULL,
  `gridIndexZ` int(11) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Movie`
--

CREATE TABLE `Movie` (
  `movieId` int(11) NOT NULL,
  `dataCollectionId` int(11) UNSIGNED DEFAULT NULL,
  `movieNumber` int(11) DEFAULT NULL,
  `movieFullPath` varchar(255) DEFAULT NULL,
  `positionX` varchar(45) DEFAULT NULL,
  `positionY` varchar(45) DEFAULT NULL,
  `micrographFullPath` varchar(255) DEFAULT NULL,
  `micrographSnapshotFullPath` varchar(255) DEFAULT NULL,
  `xmlMetaDataFullPath` varchar(255) DEFAULT NULL,
  `dosePerImage` varchar(45) DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MXMRRun`
--

CREATE TABLE `MXMRRun` (
  `mxMRRunId` int(11) UNSIGNED NOT NULL,
  `autoProcScalingId` int(11) UNSIGNED NOT NULL,
  `success` tinyint(1) DEFAULT 0 COMMENT 'Indicates whether the program completed. 1 for success, 0 for failure.',
  `message` varchar(255) DEFAULT NULL COMMENT 'A short summary of the findings, success or failure.',
  `pipeline` varchar(50) DEFAULT NULL,
  `inputCoordFile` varchar(255) DEFAULT NULL,
  `outputCoordFile` varchar(255) DEFAULT NULL,
  `inputMTZFile` varchar(255) DEFAULT NULL,
  `outputMTZFile` varchar(255) DEFAULT NULL,
  `runDirectory` varchar(255) DEFAULT NULL,
  `logFile` varchar(255) DEFAULT NULL,
  `commandLine` varchar(255) DEFAULT NULL,
  `rValueStart` float DEFAULT NULL,
  `rValueEnd` float DEFAULT NULL,
  `rFreeValueStart` float DEFAULT NULL,
  `rFreeValueEnd` float DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `MXMRRunBlob`
--

CREATE TABLE `MXMRRunBlob` (
  `mxMRRunBlobId` int(11) UNSIGNED NOT NULL,
  `mxMRRunId` int(11) UNSIGNED NOT NULL,
  `view1` varchar(255) DEFAULT NULL,
  `view2` varchar(255) DEFAULT NULL,
  `view3` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Particle`
--

CREATE TABLE `Particle` (
  `particleId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ParticleClassification`
--

CREATE TABLE `ParticleClassification` (
  `particleClassificationId` int(10) UNSIGNED NOT NULL,
  `particleClassificationGroupId` int(10) UNSIGNED DEFAULT NULL,
  `classNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'Identified of the class. A unique ID given by Relion',
  `classImageFullPath` varchar(255) DEFAULT NULL COMMENT 'The PNG of the class',
  `particlesPerClass` int(10) UNSIGNED DEFAULT NULL COMMENT 'Number of particles within the selected class, can then be used together with the total number above to calculate the percentage',
  `classDistribution` float DEFAULT NULL,
  `rotationAccuracy` float UNSIGNED DEFAULT NULL,
  `translationAccuracy` float DEFAULT NULL COMMENT 'Unit: Angstroms',
  `estimatedResolution` float DEFAULT NULL COMMENT 'Unit: Angstroms',
  `overallFourierCompleteness` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Results of 2D or 3D classification';

-- --------------------------------------------------------

--
-- Table structure for table `ParticleClassificationGroup`
--

CREATE TABLE `ParticleClassificationGroup` (
  `particleClassificationGroupId` int(10) UNSIGNED NOT NULL,
  `particlePickerId` int(10) UNSIGNED DEFAULT NULL,
  `programId` int(10) UNSIGNED DEFAULT NULL,
  `type` enum('2D','3D') DEFAULT NULL COMMENT 'Indicates the type of particle classification',
  `batchNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'Corresponding to batch number',
  `numberOfParticlesPerBatch` int(10) UNSIGNED DEFAULT NULL COMMENT 'total number of particles per batch (a large integer)',
  `numberOfClassesPerBatch` int(10) UNSIGNED DEFAULT NULL,
  `symmetry` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ParticleClassification_has_CryoemInitialModel`
--

CREATE TABLE `ParticleClassification_has_CryoemInitialModel` (
  `particleClassificationId` int(10) UNSIGNED NOT NULL,
  `cryoemInitialModelId` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ParticlePicker`
--

CREATE TABLE `ParticlePicker` (
  `particlePickerId` int(10) UNSIGNED NOT NULL,
  `programId` int(10) UNSIGNED DEFAULT NULL,
  `firstMotionCorrectionId` int(11) DEFAULT NULL,
  `particlePickingTemplate` varchar(255) DEFAULT NULL COMMENT 'Cryolo model',
  `particleDiameter` float DEFAULT NULL COMMENT 'Unit: nm',
  `numberOfParticles` int(10) UNSIGNED DEFAULT NULL,
  `summaryImageFullPath` varchar(255) DEFAULT NULL COMMENT 'Generated summary micrograph image with highlighted particles'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='An instance of a particle picker program that was run';

-- --------------------------------------------------------

--
-- Table structure for table `PDB`
--

CREATE TABLE `PDB` (
  `pdbId` int(11) UNSIGNED NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `contents` mediumtext DEFAULT NULL,
  `code` varchar(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PDBEntry`
--

CREATE TABLE `PDBEntry` (
  `pdbEntryId` int(11) UNSIGNED NOT NULL,
  `autoProcProgramId` int(11) UNSIGNED DEFAULT NULL,
  `code` varchar(4) DEFAULT NULL,
  `cell_a` float DEFAULT NULL,
  `cell_b` float DEFAULT NULL,
  `cell_c` float DEFAULT NULL,
  `cell_alpha` float DEFAULT NULL,
  `cell_beta` float DEFAULT NULL,
  `cell_gamma` float DEFAULT NULL,
  `resolution` float DEFAULT NULL,
  `pdbTitle` varchar(255) DEFAULT NULL,
  `pdbAuthors` varchar(600) DEFAULT NULL,
  `pdbDate` datetime DEFAULT NULL,
  `pdbBeamlineName` varchar(50) DEFAULT NULL,
  `beamlines` varchar(100) DEFAULT NULL,
  `distance` float DEFAULT NULL,
  `autoProcCount` smallint(6) DEFAULT NULL,
  `dataCollectionCount` smallint(6) DEFAULT NULL,
  `beamlineMatch` tinyint(1) DEFAULT NULL,
  `authorMatch` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PDBEntry_has_AutoProcProgram`
--

CREATE TABLE `PDBEntry_has_AutoProcProgram` (
  `pdbEntryHasAutoProcId` int(11) UNSIGNED NOT NULL,
  `pdbEntryId` int(11) UNSIGNED NOT NULL,
  `autoProcProgramId` int(11) UNSIGNED NOT NULL,
  `distance` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Permission`
--

CREATE TABLE `Permission` (
  `permissionId` int(11) UNSIGNED NOT NULL,
  `type` varchar(15) NOT NULL,
  `description` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Person`
--

CREATE TABLE `Person` (
  `personId` int(10) UNSIGNED NOT NULL,
  `laboratoryId` int(10) UNSIGNED DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `personUUID` varchar(45) DEFAULT NULL,
  `familyName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `givenName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `emailAddress` varchar(60) DEFAULT NULL,
  `phoneNumber` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login` varchar(45) DEFAULT NULL,
  `passwd` varchar(45) DEFAULT NULL,
  `faxNumber` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time',
  `externalId` binary(16) DEFAULT NULL,
  `cache` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Phasing`
--

CREATE TABLE `Phasing` (
  `phasingId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related spaceGroup',
  `method` enum('solvent flattening','solvent flipping') DEFAULT NULL COMMENT 'phasing method',
  `solventContent` double DEFAULT NULL,
  `enantiomorph` tinyint(1) DEFAULT NULL COMMENT '0 or 1',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingAnalysis`
--

CREATE TABLE `PhasingAnalysis` (
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingProgramAttachment`
--

CREATE TABLE `PhasingProgramAttachment` (
  `phasingProgramAttachmentId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `fileType` enum('DSIGMA_RESOLUTION','OCCUPANCY_SITENUMBER','CONTRAST_CYCLE','CCALL_CCWEAK','IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT') DEFAULT NULL COMMENT 'file type',
  `fileName` varchar(45) DEFAULT NULL COMMENT 'file name',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'file path',
  `input` tinyint(1) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingProgramRun`
--

CREATE TABLE `PhasingProgramRun` (
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingCommandLine` varchar(255) DEFAULT NULL COMMENT 'Command line for phasing',
  `phasingPrograms` varchar(255) DEFAULT NULL COMMENT 'Phasing programs (comma separated)',
  `phasingStatus` tinyint(1) DEFAULT NULL COMMENT 'success (1) / fail (0)',
  `phasingMessage` varchar(255) DEFAULT NULL COMMENT 'warning, error,...',
  `phasingStartTime` datetime DEFAULT NULL COMMENT 'Processing start time',
  `phasingEndTime` datetime DEFAULT NULL COMMENT 'Processing end time',
  `phasingEnvironment` varchar(255) DEFAULT NULL COMMENT 'Cpus, Nodes,...',
  `phasingDirectory` varchar(255) DEFAULT NULL COMMENT 'Directory of execution',
  `recordTimeStamp` timestamp NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingStatistics`
--

CREATE TABLE `PhasingStatistics` (
  `phasingStatisticsId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingHasScalingId1` int(11) UNSIGNED DEFAULT NULL COMMENT 'the dataset in question',
  `phasingHasScalingId2` int(11) UNSIGNED DEFAULT NULL COMMENT 'if this is MIT or MAD, which scaling are being compared, null otherwise',
  `phasingStepId` int(10) UNSIGNED DEFAULT NULL,
  `numberOfBins` int(11) DEFAULT NULL COMMENT 'the total number of bins',
  `binNumber` int(11) DEFAULT NULL COMMENT 'binNumber, 999 for overall',
  `lowRes` double DEFAULT NULL COMMENT 'low resolution cutoff of this binfloat',
  `highRes` double DEFAULT NULL COMMENT 'high resolution cutoff of this binfloat',
  `metric` enum('Rcullis','Average Fragment Length','Chain Count','Residues Count','CC','PhasingPower','FOM','<d"/sig>','Best CC','CC(1/2)','Weak CC','CFOM','Pseudo_free_CC','CC of partial model','Start R-work','Start R-free','Final R-work','Final R-free') DEFAULT NULL COMMENT 'metric',
  `statisticsValue` double DEFAULT NULL COMMENT 'the statistics value',
  `nReflections` int(11) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingStep`
--

CREATE TABLE `PhasingStep` (
  `phasingStepId` int(10) UNSIGNED NOT NULL,
  `previousPhasingStepId` int(10) UNSIGNED DEFAULT NULL,
  `programRunId` int(10) UNSIGNED DEFAULT NULL,
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL,
  `autoProcScalingId` int(10) UNSIGNED DEFAULT NULL,
  `phasingAnalysisId` int(10) UNSIGNED DEFAULT NULL,
  `phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT') DEFAULT NULL,
  `method` varchar(45) DEFAULT NULL,
  `solventContent` varchar(45) DEFAULT NULL,
  `enantiomorph` varchar(45) DEFAULT NULL,
  `lowRes` varchar(45) DEFAULT NULL,
  `highRes` varchar(45) DEFAULT NULL,
  `groupName` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Phasing_has_Scaling`
--

CREATE TABLE `Phasing_has_Scaling` (
  `phasingHasScalingId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `autoProcScalingId` int(10) UNSIGNED NOT NULL COMMENT 'Related autoProcScaling item',
  `datasetNumber` int(11) DEFAULT NULL COMMENT 'serial number of the dataset and always reserve 0 for the reference',
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PHPSession`
--

CREATE TABLE `PHPSession` (
  `id` varchar(50) NOT NULL,
  `accessDate` datetime DEFAULT NULL,
  `data` varchar(4000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PlateGroup`
--

CREATE TABLE `PlateGroup` (
  `plateGroupId` int(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PlateType`
--

CREATE TABLE `PlateType` (
  `PlateTypeId` int(10) NOT NULL,
  `experimentId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `shape` varchar(45) DEFAULT NULL,
  `rowCount` int(11) DEFAULT NULL,
  `columnCount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Position`
--

CREATE TABLE `Position` (
  `positionId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `relativePositionId` int(11) UNSIGNED DEFAULT NULL COMMENT 'relative position, null otherwise',
  `posX` double DEFAULT NULL,
  `posY` double DEFAULT NULL,
  `posZ` double DEFAULT NULL,
  `scale` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Positioner`
--

CREATE TABLE `Positioner` (
  `positionerId` int(10) NOT NULL,
  `positioner` varchar(50) NOT NULL,
  `value` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='An arbitrary positioner and its value, could be e.g. a motor. Allows for instance to store some positions with a sample or subsample';

-- --------------------------------------------------------

--
-- Table structure for table `PreparePhasingData`
--

CREATE TABLE `PreparePhasingData` (
  `preparePhasingDataId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related spaceGroup',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ProcessingPipeline`
--

CREATE TABLE `ProcessingPipeline` (
  `processingPipelineId` int(11) NOT NULL,
  `processingPipelineCategoryId` int(11) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `discipline` varchar(10) NOT NULL,
  `pipelineStatus` enum('automatic','optional','deprecated') DEFAULT NULL COMMENT 'Is the pipeline in operation or available',
  `reprocessing` tinyint(1) DEFAULT 1 COMMENT 'Pipeline is available for re-processing'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='A lookup table for different processing pipelines and their categories';

-- --------------------------------------------------------

--
-- Table structure for table `ProcessingPipelineCategory`
--

CREATE TABLE `ProcessingPipelineCategory` (
  `processingPipelineCategoryId` int(11) NOT NULL,
  `name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='A lookup table for the category of processing pipeline';

-- --------------------------------------------------------

--
-- Table structure for table `Project`
--

CREATE TABLE `Project` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `personId` int(11) UNSIGNED DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `acronym` varchar(100) DEFAULT NULL,
  `owner` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_BLSample`
--

CREATE TABLE `Project_has_BLSample` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `blSampleId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_DCGroup`
--

CREATE TABLE `Project_has_DCGroup` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `dataCollectionGroupId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_EnergyScan`
--

CREATE TABLE `Project_has_EnergyScan` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `energyScanId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_Person`
--

CREATE TABLE `Project_has_Person` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `personId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_Protein`
--

CREATE TABLE `Project_has_Protein` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `proteinId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_Session`
--

CREATE TABLE `Project_has_Session` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `sessionId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_Shipping`
--

CREATE TABLE `Project_has_Shipping` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `shippingId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_User`
--

CREATE TABLE `Project_has_User` (
  `projecthasuserid` int(11) UNSIGNED NOT NULL,
  `projectid` int(11) UNSIGNED NOT NULL,
  `username` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_XFEFSpectrum`
--

CREATE TABLE `Project_has_XFEFSpectrum` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `xfeFluorescenceSpectrumId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Proposal`
--

CREATE TABLE `Proposal` (
  `proposalId` int(10) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `proposalCode` varchar(45) DEFAULT NULL,
  `proposalNumber` varchar(45) DEFAULT NULL,
  `proposalType` varchar(2) DEFAULT NULL COMMENT 'Proposal type: MX, BX',
  `bltimeStamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `externalId` binary(16) DEFAULT NULL,
  `state` enum('Open','Closed','Cancelled') DEFAULT 'Open'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ProposalHasPerson`
--

CREATE TABLE `ProposalHasPerson` (
  `proposalId` int(10) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Protein`
--

CREATE TABLE `Protein` (
  `proteinId` int(10) UNSIGNED NOT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `description` text DEFAULT NULL COMMENT 'A description/summary using words and sentences',
  `hazardGroup` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT 'A.k.a. risk group',
  `containmentLevel` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT 'A.k.a. biosafety level, which indicates the level of containment required',
  `safetyLevel` enum('GREEN','YELLOW','RED') DEFAULT NULL,
  `molecularMass` double DEFAULT NULL,
  `proteinType` varchar(45) DEFAULT NULL,
  `sequence` text DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `bltimeStamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `isCreatedBySampleSheet` tinyint(1) DEFAULT 0,
  `externalId` binary(16) DEFAULT NULL,
  `componentTypeId` int(11) UNSIGNED DEFAULT NULL,
  `modId` varchar(20) DEFAULT NULL,
  `concentrationTypeId` int(11) UNSIGNED DEFAULT NULL,
  `global` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Protein_has_Lattice`
--

CREATE TABLE `Protein_has_Lattice` (
  `proteinId` int(10) UNSIGNED NOT NULL,
  `cell_a` double DEFAULT NULL,
  `cell_b` double DEFAULT NULL,
  `cell_c` double DEFAULT NULL,
  `cell_alpha` double DEFAULT NULL,
  `cell_beta` double DEFAULT NULL,
  `cell_gamma` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Protein_has_PDB`
--

CREATE TABLE `Protein_has_PDB` (
  `proteinhaspdbid` int(11) UNSIGNED NOT NULL,
  `proteinid` int(11) UNSIGNED NOT NULL,
  `pdbid` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PurificationColumn`
--

CREATE TABLE `PurificationColumn` (
  `purificationColumnId` int(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `active` tinyint(1) DEFAULT 1 COMMENT '1=active, 0=inactive'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Size exclusion chromotography (SEC) lookup table for BioSAXS';

-- --------------------------------------------------------

--
-- Table structure for table `RigidBodyModeling`
--

CREATE TABLE `RigidBodyModeling` (
  `rigidBodyModelingId` int(11) NOT NULL,
  `subtractionId` int(11) NOT NULL,
  `fitFilePath` varchar(255) DEFAULT NULL,
  `rigidBodyModelFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `curveConfigFilePath` varchar(255) DEFAULT NULL,
  `subUnitConfigFilePath` varchar(255) DEFAULT NULL,
  `crossCorrConfigFilePath` varchar(255) DEFAULT NULL,
  `contactDescriptionFilePath` varchar(255) DEFAULT NULL,
  `symmetry` varchar(255) DEFAULT NULL,
  `creationDate` varchar(45) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `RobotAction`
--

CREATE TABLE `RobotAction` (
  `robotActionId` int(11) UNSIGNED NOT NULL,
  `blsessionId` int(11) UNSIGNED NOT NULL,
  `blsampleId` int(11) UNSIGNED DEFAULT NULL,
  `actionType` enum('LOAD','UNLOAD','DISPOSE','STORE','WASH','ANNEAL') DEFAULT NULL,
  `startTimestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `endTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` enum('SUCCESS','ERROR','CRITICAL','WARNING','COMMANDNOTSENT') DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `containerLocation` smallint(6) DEFAULT NULL,
  `dewarLocation` smallint(6) DEFAULT NULL,
  `sampleBarcode` varchar(45) DEFAULT NULL,
  `xtalSnapshotBefore` varchar(255) DEFAULT NULL,
  `xtalSnapshotAfter` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Robot actions as reported by MXCube';

-- --------------------------------------------------------

--
-- Table structure for table `Run`
--

CREATE TABLE `Run` (
  `runId` int(10) NOT NULL,
  `timePerFrame` varchar(45) DEFAULT NULL,
  `timeStart` varchar(45) DEFAULT NULL,
  `timeEnd` varchar(45) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL,
  `exposureTemperature` varchar(45) DEFAULT NULL,
  `spectrophotometer` varchar(45) DEFAULT NULL,
  `energy` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `frameAverage` varchar(45) DEFAULT NULL,
  `frameCount` varchar(45) DEFAULT NULL,
  `transmission` varchar(45) DEFAULT NULL,
  `beamCenterX` varchar(45) DEFAULT NULL,
  `beamCenterY` varchar(45) DEFAULT NULL,
  `pixelSizeX` varchar(45) DEFAULT NULL,
  `pixelSizeY` varchar(45) DEFAULT NULL,
  `radiationRelative` varchar(45) DEFAULT NULL,
  `radiationAbsolute` varchar(45) DEFAULT NULL,
  `normalization` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SafetyLevel`
--

CREATE TABLE `SafetyLevel` (
  `safetyLevelId` int(10) NOT NULL,
  `code` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SAFETYREQUEST`
--

CREATE TABLE `SAFETYREQUEST` (
  `SAFETYREQUESTID` decimal(10,0) DEFAULT NULL,
  `XMLDOCUMENTID` decimal(10,0) DEFAULT NULL,
  `PROTEINID` decimal(10,0) DEFAULT NULL,
  `PROJECTCODE` varchar(45) DEFAULT NULL,
  `SUBMISSIONDATE` datetime DEFAULT NULL,
  `RESPONSE` decimal(3,0) DEFAULT NULL,
  `REPONSEDATE` datetime DEFAULT NULL,
  `RESPONSEDETAILS` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SAMPLECELL`
--

CREATE TABLE `SAMPLECELL` (
  `SAMPLECELLID` int(11) NOT NULL,
  `SAMPLEEXPOSUREUNITID` int(11) DEFAULT NULL,
  `ID` varchar(45) DEFAULT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `DIAMETER` varchar(45) DEFAULT NULL,
  `MATERIAL` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SAMPLEEXPOSUREUNIT`
--

CREATE TABLE `SAMPLEEXPOSUREUNIT` (
  `SAMPLEEXPOSUREUNITID` int(11) NOT NULL,
  `ID` varchar(45) DEFAULT NULL,
  `PATHLENGTH` varchar(45) DEFAULT NULL,
  `VOLUME` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SamplePlate`
--

CREATE TABLE `SamplePlate` (
  `samplePlateId` int(10) NOT NULL,
  `experimentId` int(10) NOT NULL,
  `plateGroupId` int(10) DEFAULT NULL,
  `plateTypeId` int(10) DEFAULT NULL,
  `instructionSetId` int(10) DEFAULT NULL,
  `boxId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `slotPositionRow` varchar(45) DEFAULT NULL,
  `slotPositionColumn` varchar(45) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SamplePlatePosition`
--

CREATE TABLE `SamplePlatePosition` (
  `samplePlatePositionId` int(10) NOT NULL,
  `samplePlateId` int(10) NOT NULL,
  `rowNumber` int(11) DEFAULT NULL,
  `columnNumber` int(11) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SaxsDataCollection`
--

CREATE TABLE `SaxsDataCollection` (
  `dataCollectionId` int(10) NOT NULL,
  `experimentId` int(10) NOT NULL,
  `comments` varchar(5120) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SAXSDATACOLLECTIONGROUP`
--

CREATE TABLE `SAXSDATACOLLECTIONGROUP` (
  `DATACOLLECTIONGROUPID` int(11) NOT NULL,
  `DEFAULTDATAACQUISITIONID` int(11) DEFAULT NULL,
  `SAXSDATACOLLECTIONARRAYID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScanParametersModel`
--

CREATE TABLE `ScanParametersModel` (
  `scanParametersModelId` int(11) UNSIGNED NOT NULL,
  `scanParametersServiceId` int(10) UNSIGNED DEFAULT NULL,
  `dataCollectionPlanId` int(11) UNSIGNED DEFAULT NULL,
  `modelNumber` tinyint(3) UNSIGNED DEFAULT NULL,
  `start` double DEFAULT NULL,
  `stop` double DEFAULT NULL,
  `step` double DEFAULT NULL,
  `array` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScanParametersService`
--

CREATE TABLE `ScanParametersService` (
  `scanParametersServiceId` int(10) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Schedule`
--

CREATE TABLE `Schedule` (
  `scheduleId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScheduleComponent`
--

CREATE TABLE `ScheduleComponent` (
  `scheduleComponentId` int(11) UNSIGNED NOT NULL,
  `scheduleId` int(11) UNSIGNED NOT NULL,
  `inspectionTypeId` int(11) UNSIGNED DEFAULT NULL,
  `offset_hours` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SchemaStatus`
--

CREATE TABLE `SchemaStatus` (
  `schemaStatusId` int(11) NOT NULL,
  `scriptName` varchar(100) NOT NULL,
  `schemaStatus` varchar(10) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Screen`
--

CREATE TABLE `Screen` (
  `screenId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `global` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreenComponent`
--

CREATE TABLE `ScreenComponent` (
  `screenComponentId` int(11) UNSIGNED NOT NULL,
  `screenComponentGroupId` int(11) UNSIGNED NOT NULL,
  `componentId` int(11) UNSIGNED DEFAULT NULL,
  `concentration` float DEFAULT NULL,
  `pH` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreenComponentGroup`
--

CREATE TABLE `ScreenComponentGroup` (
  `screenComponentGroupId` int(11) UNSIGNED NOT NULL,
  `screenId` int(11) UNSIGNED NOT NULL,
  `position` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Screening`
--

CREATE TABLE `Screening` (
  `screeningId` int(10) UNSIGNED NOT NULL,
  `diffractionPlanId` int(10) UNSIGNED DEFAULT NULL COMMENT 'references DiffractionPlan',
  `dataCollectionGroupId` int(11) DEFAULT NULL,
  `dataCollectionId` int(11) UNSIGNED DEFAULT NULL,
  `bltimeStamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `programVersion` varchar(45) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `shortComments` varchar(20) DEFAULT NULL,
  `xmlSampleInformation` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningInput`
--

CREATE TABLE `ScreeningInput` (
  `screeningInputId` int(10) UNSIGNED NOT NULL,
  `screeningId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `diffractionPlanId` int(10) DEFAULT NULL COMMENT 'references DiffractionPlan table',
  `beamX` float DEFAULT NULL,
  `beamY` float DEFAULT NULL,
  `rmsErrorLimits` float DEFAULT NULL,
  `minimumFractionIndexed` float DEFAULT NULL,
  `maximumFractionRejected` float DEFAULT NULL,
  `minimumSignalToNoise` float DEFAULT NULL,
  `xmlSampleInformation` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningOutput`
--

CREATE TABLE `ScreeningOutput` (
  `screeningOutputId` int(10) UNSIGNED NOT NULL,
  `screeningId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `statusDescription` varchar(1024) DEFAULT NULL,
  `rejectedReflections` int(10) UNSIGNED DEFAULT NULL,
  `resolutionObtained` float DEFAULT NULL,
  `spotDeviationR` float DEFAULT NULL,
  `spotDeviationTheta` float DEFAULT NULL,
  `beamShiftX` float DEFAULT NULL,
  `beamShiftY` float DEFAULT NULL,
  `numSpotsFound` int(10) UNSIGNED DEFAULT NULL,
  `numSpotsUsed` int(10) UNSIGNED DEFAULT NULL,
  `numSpotsRejected` int(10) UNSIGNED DEFAULT NULL,
  `mosaicity` float DEFAULT NULL,
  `iOverSigma` float DEFAULT NULL,
  `diffractionRings` tinyint(1) DEFAULT NULL,
  `strategySuccess` tinyint(1) NOT NULL DEFAULT 0,
  `mosaicityEstimated` tinyint(1) NOT NULL DEFAULT 0,
  `rankingResolution` double DEFAULT NULL,
  `program` varchar(45) DEFAULT NULL,
  `doseTotal` double DEFAULT NULL,
  `totalExposureTime` double DEFAULT NULL,
  `totalRotationRange` double DEFAULT NULL,
  `totalNumberOfImages` int(11) DEFAULT NULL,
  `rFriedel` double DEFAULT NULL,
  `indexingSuccess` tinyint(1) NOT NULL DEFAULT 0,
  `screeningSuccess` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningOutputLattice`
--

CREATE TABLE `ScreeningOutputLattice` (
  `screeningOutputLatticeId` int(10) UNSIGNED NOT NULL,
  `screeningOutputId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `spaceGroup` varchar(45) DEFAULT NULL,
  `pointGroup` varchar(45) DEFAULT NULL,
  `bravaisLattice` varchar(45) DEFAULT NULL,
  `rawOrientationMatrix_a_x` float DEFAULT NULL,
  `rawOrientationMatrix_a_y` float DEFAULT NULL,
  `rawOrientationMatrix_a_z` float DEFAULT NULL,
  `rawOrientationMatrix_b_x` float DEFAULT NULL,
  `rawOrientationMatrix_b_y` float DEFAULT NULL,
  `rawOrientationMatrix_b_z` float DEFAULT NULL,
  `rawOrientationMatrix_c_x` float DEFAULT NULL,
  `rawOrientationMatrix_c_y` float DEFAULT NULL,
  `rawOrientationMatrix_c_z` float DEFAULT NULL,
  `unitCell_a` float DEFAULT NULL,
  `unitCell_b` float DEFAULT NULL,
  `unitCell_c` float DEFAULT NULL,
  `unitCell_alpha` float DEFAULT NULL,
  `unitCell_beta` float DEFAULT NULL,
  `unitCell_gamma` float DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT current_timestamp(),
  `labelitIndexing` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningRank`
--

CREATE TABLE `ScreeningRank` (
  `screeningRankId` int(10) UNSIGNED NOT NULL,
  `screeningRankSetId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `screeningId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `rankValue` float DEFAULT NULL,
  `rankInformation` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningRankSet`
--

CREATE TABLE `ScreeningRankSet` (
  `screeningRankSetId` int(10) UNSIGNED NOT NULL,
  `rankEngine` varchar(255) DEFAULT NULL,
  `rankingProjectFileName` varchar(255) DEFAULT NULL,
  `rankingSummaryFileName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategy`
--

CREATE TABLE `ScreeningStrategy` (
  `screeningStrategyId` int(10) UNSIGNED NOT NULL,
  `screeningOutputId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `phiStart` float DEFAULT NULL,
  `phiEnd` float DEFAULT NULL,
  `rotation` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `resolution` float DEFAULT NULL,
  `completeness` float DEFAULT NULL,
  `multiplicity` float DEFAULT NULL,
  `anomalous` tinyint(1) NOT NULL DEFAULT 0,
  `program` varchar(45) DEFAULT NULL,
  `rankingResolution` float DEFAULT NULL,
  `transmission` float DEFAULT NULL COMMENT 'Transmission for the strategy as given by the strategy program.'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategySubWedge`
--

CREATE TABLE `ScreeningStrategySubWedge` (
  `screeningStrategySubWedgeId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key',
  `screeningStrategyWedgeId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Foreign key to parent table',
  `subWedgeNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'The number of this subwedge within the wedge',
  `rotationAxis` varchar(45) DEFAULT NULL COMMENT 'Angle where subwedge starts',
  `axisStart` float DEFAULT NULL COMMENT 'Angle where subwedge ends',
  `axisEnd` float DEFAULT NULL COMMENT 'Exposure time for subwedge',
  `exposureTime` float DEFAULT NULL COMMENT 'Transmission for subwedge',
  `transmission` float DEFAULT NULL,
  `oscillationRange` float DEFAULT NULL,
  `completeness` float DEFAULT NULL,
  `multiplicity` float DEFAULT NULL,
  `doseTotal` float DEFAULT NULL COMMENT 'Total dose for this subwedge',
  `numberOfImages` int(10) UNSIGNED DEFAULT NULL COMMENT 'Number of images for this subwedge',
  `comments` varchar(255) DEFAULT NULL,
  `resolution` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategyWedge`
--

CREATE TABLE `ScreeningStrategyWedge` (
  `screeningStrategyWedgeId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key',
  `screeningStrategyId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Foreign key to parent table',
  `wedgeNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'The number of this wedge within the strategy',
  `resolution` float DEFAULT NULL,
  `completeness` float DEFAULT NULL,
  `multiplicity` float DEFAULT NULL,
  `doseTotal` float DEFAULT NULL COMMENT 'Total dose for this wedge',
  `numberOfImages` int(10) UNSIGNED DEFAULT NULL COMMENT 'Number of images for this wedge',
  `phi` float DEFAULT NULL,
  `kappa` float DEFAULT NULL,
  `chi` float DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `wavelength` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SessionType`
--

CREATE TABLE `SessionType` (
  `sessionTypeId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `typeName` varchar(31) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Session_has_Person`
--

CREATE TABLE `Session_has_Person` (
  `sessionId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `personId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `role` enum('Local Contact','Local Contact 2','Staff','Team Leader','Co-Investigator','Principal Investigator','Alternate Contact') DEFAULT NULL,
  `remote` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Shipping`
--

CREATE TABLE `Shipping` (
  `shippingId` int(10) UNSIGNED NOT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `shippingName` varchar(45) DEFAULT NULL,
  `deliveryAgent_agentName` varchar(45) DEFAULT NULL,
  `deliveryAgent_shippingDate` date DEFAULT NULL,
  `deliveryAgent_deliveryDate` date DEFAULT NULL,
  `deliveryAgent_agentCode` varchar(45) DEFAULT NULL,
  `deliveryAgent_flightCode` varchar(45) DEFAULT NULL,
  `shippingStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` datetime DEFAULT NULL,
  `laboratoryId` int(10) UNSIGNED DEFAULT NULL,
  `isStorageShipping` tinyint(1) DEFAULT 0,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `sendingLabContactId` int(10) UNSIGNED DEFAULT NULL,
  `returnLabContactId` int(10) UNSIGNED DEFAULT NULL,
  `returnCourier` varchar(45) DEFAULT NULL,
  `dateOfShippingToUser` datetime DEFAULT NULL,
  `shippingType` varchar(45) DEFAULT NULL,
  `safetyLevel` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ShippingHasSession`
--

CREATE TABLE `ShippingHasSession` (
  `shippingId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Sleeve`
--

CREATE TABLE `Sleeve` (
  `sleeveId` tinyint(3) NOT NULL COMMENT 'The unique sleeve id 1...255 which also identifies its home location in the freezer',
  `location` tinyint(3) DEFAULT NULL COMMENT 'NULL == freezer, 1...255 for local storage locations',
  `lastMovedToFreezer` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastMovedFromFreezer` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Registry of ice-filled sleeves used to cool plates whilst on the goniometer';

-- --------------------------------------------------------

--
-- Table structure for table `SpaceGroup`
--

CREATE TABLE `SpaceGroup` (
  `spaceGroupId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key',
  `geometryClassnameId` int(11) UNSIGNED DEFAULT NULL,
  `spaceGroupNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'ccp4 number pr IUCR',
  `spaceGroupShortName` varchar(45) DEFAULT NULL COMMENT 'short name without blank',
  `spaceGroupName` varchar(45) DEFAULT NULL COMMENT 'verbose name',
  `bravaisLattice` varchar(45) DEFAULT NULL COMMENT 'short name',
  `bravaisLatticeName` varchar(45) DEFAULT NULL COMMENT 'verbose name',
  `pointGroup` varchar(45) DEFAULT NULL COMMENT 'point group',
  `MX_used` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1 if used in the crystal form'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Specimen`
--

CREATE TABLE `Specimen` (
  `specimenId` int(10) NOT NULL,
  `experimentId` int(10) NOT NULL,
  `bufferId` int(10) DEFAULT NULL,
  `macromoleculeId` int(10) DEFAULT NULL,
  `samplePlatePositionId` int(10) DEFAULT NULL,
  `safetyLevelId` int(10) DEFAULT NULL,
  `stockSolutionId` int(10) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `concentration` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `comments` varchar(5120) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `StockSolution`
--

CREATE TABLE `StockSolution` (
  `stockSolutionId` int(10) NOT NULL,
  `proposalId` int(10) NOT NULL DEFAULT -1,
  `bufferId` int(10) NOT NULL,
  `macromoleculeId` int(10) DEFAULT NULL,
  `instructionSetId` int(10) DEFAULT NULL,
  `boxId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `storageTemperature` varchar(55) DEFAULT NULL,
  `volume` varchar(55) DEFAULT NULL,
  `concentration` varchar(55) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Stoichiometry`
--

CREATE TABLE `Stoichiometry` (
  `stoichiometryId` int(10) NOT NULL,
  `hostMacromoleculeId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  `ratio` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Structure`
--

CREATE TABLE `Structure` (
  `structureId` int(10) NOT NULL,
  `macromoleculeId` int(10) DEFAULT NULL,
  `crystalId` int(10) UNSIGNED DEFAULT NULL,
  `blSampleId` int(10) UNSIGNED DEFAULT NULL,
  `filePath` varchar(2048) DEFAULT NULL,
  `structureType` varchar(45) DEFAULT NULL,
  `fromResiduesBases` varchar(45) DEFAULT NULL,
  `toResiduesBases` varchar(45) DEFAULT NULL,
  `sequence` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `symmetry` varchar(45) DEFAULT NULL,
  `multiplicity` varchar(45) DEFAULT NULL,
  `groupName` varchar(45) DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `uniprotId` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SubstructureDetermination`
--

CREATE TABLE `SubstructureDetermination` (
  `substructureDeterminationId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related spaceGroup',
  `method` enum('SAD','MAD','SIR','SIRAS','MR','MIR','MIRAS','RIP','RIPAS') DEFAULT NULL COMMENT 'phasing method',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Subtraction`
--

CREATE TABLE `Subtraction` (
  `subtractionId` int(10) NOT NULL,
  `dataCollectionId` int(10) NOT NULL,
  `rg` varchar(45) DEFAULT NULL,
  `rgStdev` varchar(45) DEFAULT NULL,
  `I0` varchar(45) DEFAULT NULL,
  `I0Stdev` varchar(45) DEFAULT NULL,
  `firstPointUsed` varchar(45) DEFAULT NULL,
  `lastPointUsed` varchar(45) DEFAULT NULL,
  `quality` varchar(45) DEFAULT NULL,
  `isagregated` varchar(45) DEFAULT NULL,
  `concentration` varchar(45) DEFAULT NULL,
  `gnomFilePath` varchar(255) DEFAULT NULL,
  `rgGuinier` varchar(45) DEFAULT NULL,
  `rgGnom` varchar(45) DEFAULT NULL,
  `dmax` varchar(45) DEFAULT NULL,
  `total` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `kratkyFilePath` varchar(255) DEFAULT NULL,
  `scatteringFilePath` varchar(255) DEFAULT NULL,
  `guinierFilePath` varchar(255) DEFAULT NULL,
  `substractedFilePath` varchar(255) DEFAULT NULL,
  `gnomFilePathOutput` varchar(255) DEFAULT NULL,
  `sampleOneDimensionalFiles` int(10) DEFAULT NULL,
  `bufferOnedimensionalFiles` int(10) DEFAULT NULL,
  `sampleAverageFilePath` varchar(255) DEFAULT NULL,
  `bufferAverageFilePath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SubtractionToAbInitioModel`
--

CREATE TABLE `SubtractionToAbInitioModel` (
  `subtractionToAbInitioModelId` int(10) NOT NULL,
  `abInitioId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Superposition`
--

CREATE TABLE `Superposition` (
  `superpositionId` int(11) NOT NULL,
  `subtractionId` int(11) NOT NULL,
  `abinitioModelPdbFilePath` varchar(255) DEFAULT NULL,
  `aprioriPdbFilePath` varchar(255) DEFAULT NULL,
  `alignedPdbFilePath` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SW_onceToken`
--

CREATE TABLE `SW_onceToken` (
  `onceTokenId` int(11) UNSIGNED NOT NULL,
  `token` varchar(128) DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `validity` varchar(200) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='One-time use tokens needed for token auth in order to grant access to file downloads and webcams (and some images)';

-- --------------------------------------------------------

--
-- Table structure for table `UntrustedRegion`
--

CREATE TABLE `UntrustedRegion` (
  `untrustedRegionId` int(11) NOT NULL COMMENT 'Primary key (auto-incremented)',
  `detectorId` int(11) NOT NULL,
  `x1` int(11) NOT NULL,
  `x2` int(11) NOT NULL,
  `y1` int(11) NOT NULL,
  `y2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Untrsuted region linked to a detector';

-- --------------------------------------------------------

--
-- Table structure for table `UserGroup`
--

CREATE TABLE `UserGroup` (
  `userGroupId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `UserGroup_has_Permission`
--

CREATE TABLE `UserGroup_has_Permission` (
  `userGroupId` int(11) UNSIGNED NOT NULL,
  `permissionId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `UserGroup_has_Person`
--

CREATE TABLE `UserGroup_has_Person` (
  `userGroupId` int(11) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection` (
`dataCollectionId` int(11) unsigned
,`dataCollectionGroupId` int(11)
,`strategySubWedgeOrigId` int(10) unsigned
,`detectorId` int(11)
,`blSubSampleId` int(11) unsigned
,`dataCollectionNumber` int(10) unsigned
,`startTime` datetime
,`endTime` datetime
,`runStatus` varchar(45)
,`axisStart` float
,`axisEnd` float
,`axisRange` float
,`overlap` float
,`numberOfImages` int(10) unsigned
,`startImageNumber` int(10) unsigned
,`numberOfPasses` int(10) unsigned
,`exposureTime` float
,`imageDirectory` varchar(255)
,`imagePrefix` varchar(100)
,`imageSuffix` varchar(45)
,`fileTemplate` varchar(255)
,`wavelength` float
,`resolution` float
,`detectorDistance` float
,`xBeam` float
,`yBeam` float
,`xBeamPix` float
,`yBeamPix` float
,`comments` varchar(1024)
,`printableForReport` tinyint(1) unsigned
,`slitGapVertical` float
,`slitGapHorizontal` float
,`transmission` float
,`synchrotronMode` varchar(20)
,`xtalSnapshotFullPath1` varchar(255)
,`xtalSnapshotFullPath2` varchar(255)
,`xtalSnapshotFullPath3` varchar(255)
,`xtalSnapshotFullPath4` varchar(255)
,`rotationAxis` enum('Omega','Kappa','Phi')
,`phiStart` float
,`kappaStart` float
,`omegaStart` float
,`resolutionAtCorner` float
,`detector2Theta` float
,`undulatorGap1` float
,`undulatorGap2` float
,`undulatorGap3` float
,`beamSizeAtSampleX` float
,`beamSizeAtSampleY` float
,`centeringMethod` varchar(255)
,`averageTemperature` float
,`actualCenteringPosition` varchar(255)
,`beamShape` varchar(45)
,`flux` double
,`flux_end` double
,`totalAbsorbedDose` double
,`bestWilsonPlotPath` varchar(255)
,`imageQualityIndicatorsPlotPath` varchar(512)
,`imageQualityIndicatorsCSVPath` varchar(512)
,`sessionId` int(10) unsigned
,`proposalId` int(10) unsigned
,`workflowId` int(11) unsigned
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`autoProcScalingId` int(10) unsigned
,`cell_a` float
,`cell_b` float
,`cell_c` float
,`cell_alpha` float
,`cell_beta` float
,`cell_gamma` float
,`anomalous` tinyint(1)
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitHigh` float
,`resolutionLimitLow` float
,`completeness` float
,`AutoProc_spaceGroup` varchar(45)
,`autoProcId` int(10) unsigned
,`rMerge` float
,`ccHalf` float
,`meanIOverSigI` float
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`AutoProcProgram_processingPrograms` varchar(255)
,`AutoProcProgram_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
,`AutoProcProgram_autoProcProgramId` int(10) unsigned
,`ScreeningOutput_rankingResolution` double
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_autoprocintegration`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_autoprocintegration` (
`v_datacollection_summary_phasing_autoProcIntegrationId` int(10) unsigned
,`v_datacollection_summary_phasing_dataCollectionId` int(11) unsigned
,`v_datacollection_summary_phasing_cell_a` float
,`v_datacollection_summary_phasing_cell_b` float
,`v_datacollection_summary_phasing_cell_c` float
,`v_datacollection_summary_phasing_cell_alpha` float
,`v_datacollection_summary_phasing_cell_beta` float
,`v_datacollection_summary_phasing_cell_gamma` float
,`v_datacollection_summary_phasing_anomalous` tinyint(1)
,`v_datacollection_summary_phasing_autoproc_space_group` varchar(45)
,`v_datacollection_summary_phasing_autoproc_autoprocId` int(10) unsigned
,`v_datacollection_summary_phasing_autoProcScalingId` int(10) unsigned
,`v_datacollection_processingPrograms` varchar(255)
,`v_datacollection_summary_phasing_autoProcProgramId` int(10) unsigned
,`v_datacollection_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
,`v_datacollection_processingStartTime` datetime
,`v_datacollection_processingEndTime` datetime
,`v_datacollection_summary_session_sessionId` int(10) unsigned
,`v_datacollection_summary_session_proposalId` int(10) unsigned
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`PhasingStep_phasing_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
,`SpaceGroup_spaceGroupShortName` varchar(45)
,`Protein_proteinId` int(10) unsigned
,`Protein_acronym` varchar(45)
,`BLSample_name` varchar(100)
,`DataCollection_dataCollectionNumber` int(10) unsigned
,`DataCollection_imagePrefix` varchar(100)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary` (
`DataCollectionGroup_dataCollectionGroupId` int(11)
,`DataCollectionGroup_blSampleId` int(10) unsigned
,`DataCollectionGroup_sessionId` int(10) unsigned
,`DataCollectionGroup_workflowId` int(11) unsigned
,`DataCollectionGroup_experimentType` enum('EM','SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration','Still')
,`DataCollectionGroup_startTime` datetime
,`DataCollectionGroup_endTime` datetime
,`DataCollectionGroup_comments` varchar(1024)
,`DataCollectionGroup_actualSampleBarcode` varchar(45)
,`DataCollectionGroup_xtalSnapshotFullPath` varchar(255)
,`DataCollectionGroup_crystalClass` varchar(20)
,`BLSample_blSampleId` int(10) unsigned
,`BLSample_crystalId` int(10) unsigned
,`BLSample_name` varchar(100)
,`BLSample_code` varchar(45)
,`BLSample_location` varchar(45)
,`BLSample_blSampleStatus` varchar(20)
,`BLSample_comments` varchar(1024)
,`Container_containerId` int(10) unsigned
,`BLSession_sessionId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
,`BLSession_beamLineName` varchar(45)
,`BLSession_protectedData` varchar(1024)
,`Dewar_dewarId` int(10) unsigned
,`Dewar_code` varchar(45)
,`Dewar_storageLocation` varchar(45)
,`Container_containerType` varchar(20)
,`Container_code` varchar(45)
,`Container_capacity` int(10) unsigned
,`Container_beamlineLocation` varchar(20)
,`Container_sampleChangerLocation` varchar(20)
,`Protein_proteinId` int(10) unsigned
,`Protein_name` varchar(255)
,`Protein_acronym` varchar(45)
,`DataCollection_dataCollectionId` int(11) unsigned
,`DataCollection_dataCollectionGroupId` int(11)
,`DataCollection_startTime` datetime
,`DataCollection_endTime` datetime
,`DataCollection_runStatus` varchar(45)
,`DataCollection_numberOfImages` int(10) unsigned
,`DataCollection_startImageNumber` int(10) unsigned
,`DataCollection_numberOfPasses` int(10) unsigned
,`DataCollection_exposureTime` float
,`DataCollection_imageDirectory` varchar(255)
,`DataCollection_wavelength` float
,`DataCollection_resolution` float
,`DataCollection_detectorDistance` float
,`DataCollection_xBeam` float
,`transmission` float
,`DataCollection_yBeam` float
,`DataCollection_imagePrefix` varchar(100)
,`DataCollection_comments` varchar(1024)
,`DataCollection_xtalSnapshotFullPath1` varchar(255)
,`DataCollection_xtalSnapshotFullPath2` varchar(255)
,`DataCollection_xtalSnapshotFullPath3` varchar(255)
,`DataCollection_xtalSnapshotFullPath4` varchar(255)
,`DataCollection_phiStart` float
,`DataCollection_kappaStart` float
,`DataCollection_omegaStart` float
,`DataCollection_flux` double
,`DataCollection_flux_end` double
,`DataCollection_resolutionAtCorner` float
,`DataCollection_bestWilsonPlotPath` varchar(255)
,`DataCollection_dataCollectionNumber` int(10) unsigned
,`DataCollection_axisRange` float
,`DataCollection_axisStart` float
,`DataCollection_axisEnd` float
,`DataCollection_rotationAxis` enum('Omega','Kappa','Phi')
,`DataCollection_undulatorGap1` float
,`DataCollection_undulatorGap2` float
,`DataCollection_undulatorGap3` float
,`beamSizeAtSampleX` float
,`beamSizeAtSampleY` float
,`DataCollection_slitGapVertical` float
,`DataCollection_slitGapHorizontal` float
,`DataCollection_beamShape` varchar(45)
,`DataCollection_voltage` float
,`DataCollection_xBeamPix` float
,`Workflow_workflowTitle` varchar(255)
,`Workflow_workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF','MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile')
,`Workflow_status` varchar(255)
,`Workflow_workflowId` int(11) unsigned
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`autoProcScalingId` int(10) unsigned
,`cell_a` float
,`cell_b` float
,`cell_c` float
,`cell_alpha` float
,`cell_beta` float
,`cell_gamma` float
,`anomalous` tinyint(1)
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitHigh` float
,`resolutionLimitLow` float
,`completeness` float
,`AutoProc_spaceGroup` varchar(45)
,`autoProcId` int(10) unsigned
,`rMerge` float
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`AutoProcProgram_processingPrograms` varchar(255)
,`AutoProcProgram_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
,`AutoProcProgram_autoProcProgramId` int(10) unsigned
,`Screening_screeningId` int(10) unsigned
,`Screening_dataCollectionId` int(11) unsigned
,`Screening_dataCollectionGroupId` int(11)
,`ScreeningOutput_strategySuccess` tinyint(1)
,`ScreeningOutput_indexingSuccess` tinyint(1)
,`ScreeningOutput_rankingResolution` double
,`ScreeningOutput_mosaicity` float
,`ScreeningOutputLattice_spaceGroup` varchar(45)
,`ScreeningOutputLattice_unitCell_a` float
,`ScreeningOutputLattice_unitCell_b` float
,`ScreeningOutputLattice_unitCell_c` float
,`ScreeningOutputLattice_unitCell_alpha` float
,`ScreeningOutputLattice_unitCell_beta` float
,`ScreeningOutputLattice_unitCell_gamma` float
,`ScreeningOutput_totalExposureTime` double
,`ScreeningOutput_totalRotationRange` double
,`ScreeningOutput_totalNumberOfImages` int(11)
,`ScreeningStrategySubWedge_exposureTime` float
,`ScreeningStrategySubWedge_transmission` float
,`ScreeningStrategySubWedge_oscillationRange` float
,`ScreeningStrategySubWedge_numberOfImages` int(10) unsigned
,`ScreeningStrategySubWedge_multiplicity` float
,`ScreeningStrategySubWedge_completeness` float
,`ScreeningStrategySubWedge_axisStart` float
,`Shipping_shippingId` int(10) unsigned
,`Shipping_shippingName` varchar(45)
,`Shipping_shippingStatus` varchar(45)
,`diffractionPlanId` int(10) unsigned
,`experimentKind` enum('Default','MXPressE','MXPressF','MXPressO','MXPressP','MXPressP_SAD','MXPressI','MXPressE_SAD','MXScore','MXPressM','MAD','SAD','Fixed','Ligand binding','Refinement','OSC','MAD - Inverse Beam','SAD - Inverse Beam')
,`observedResolution` float
,`minimalResolution` float
,`exposureTime` float
,`oscillationRange` float
,`maximalResolution` float
,`screeningResolution` float
,`radiationSensitivity` float
,`anomalousScatterer` varchar(255)
,`preferredBeamSizeX` float
,`preferredBeamSizeY` float
,`preferredBeamDiameter` float
,`DiffractipnPlan_comments` varchar(1024)
,`aimedCompleteness` double
,`aimedIOverSigmaAtHighestRes` double
,`aimedMultiplicity` double
,`aimedResolution` double
,`anomalousData` tinyint(1)
,`complexity` varchar(45)
,`estimateRadiationDamage` tinyint(1)
,`forcedSpaceGroup` varchar(45)
,`requiredCompleteness` double
,`requiredMultiplicity` double
,`requiredResolution` double
,`strategyOption` varchar(45)
,`kappaStrategyOption` varchar(45)
,`numberOfPositions` int(11)
,`minDimAccrossSpindleAxis` double
,`maxDimAccrossSpindleAxis` double
,`radiationSensitivityBeta` double
,`radiationSensitivityGamma` double
,`minOscWidth` float
,`Detector_detectorType` varchar(255)
,`Detector_detectorManufacturer` varchar(255)
,`Detector_detectorModel` varchar(255)
,`Detector_detectorPixelSizeHorizontal` float
,`Detector_detectorPixelSizeVertical` float
,`Detector_detectorSerialNumber` varchar(30)
,`Detector_detectorDistanceMin` double
,`Detector_detectorDistanceMax` double
,`Detector_trustedPixelValueRangeLower` double
,`Detector_trustedPixelValueRangeUpper` double
,`Detector_sensorThickness` float
,`Detector_overload` float
,`Detector_XGeoCorr` varchar(255)
,`Detector_YGeoCorr` varchar(255)
,`Detector_detectorMode` varchar(255)
,`BeamLineSetup_undulatorType1` varchar(45)
,`BeamLineSetup_undulatorType2` varchar(45)
,`BeamLineSetup_undulatorType3` varchar(45)
,`BeamLineSetup_synchrotronName` varchar(255)
,`BeamLineSetup_synchrotronMode` varchar(255)
,`BeamLineSetup_polarisation` float
,`BeamLineSetup_focusingOptic` varchar(255)
,`BeamLineSetup_beamDivergenceHorizontal` float
,`BeamLineSetup_beamDivergenceVertical` float
,`BeamLineSetup_monochromatorType` varchar(255)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_autoprocintegration`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary_autoprocintegration` (
`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`cell_a` float
,`cell_b` float
,`cell_c` float
,`cell_alpha` float
,`cell_beta` float
,`cell_gamma` float
,`anomalous` tinyint(1)
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`v_datacollection_summary_autoprocintegration_processingPrograms` varchar(255)
,`AutoProcProgram_autoProcProgramId` int(10) unsigned
,`v_datacollection_summary_autoprocintegration_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
,`AutoProcIntegration_phasing_dataCollectionId` int(11) unsigned
,`PhasingStep_phasing_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
,`SpaceGroup_spaceGroupShortName` varchar(45)
,`autoProcId` int(10) unsigned
,`AutoProc_spaceGroup` varchar(45)
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitHigh` float
,`resolutionLimitLow` float
,`rMerge` float
,`meanIOverSigI` float
,`ccHalf` float
,`completeness` float
,`autoProcScalingId` int(10) unsigned
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_datacollectiongroup`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary_datacollectiongroup` (
`DataCollectionGroup_dataCollectionGroupId` int(11)
,`DataCollectionGroup_blSampleId` int(10) unsigned
,`DataCollectionGroup_sessionId` int(10) unsigned
,`DataCollectionGroup_workflowId` int(11) unsigned
,`DataCollectionGroup_experimentType` enum('EM','SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration','Still')
,`DataCollectionGroup_startTime` datetime
,`DataCollectionGroup_endTime` datetime
,`DataCollectionGroup_comments` varchar(1024)
,`DataCollectionGroup_actualSampleBarcode` varchar(45)
,`DataCollectionGroup_xtalSnapshotFullPath` varchar(255)
,`BLSample_blSampleId` int(10) unsigned
,`BLSample_crystalId` int(10) unsigned
,`BLSample_name` varchar(100)
,`BLSample_code` varchar(45)
,`BLSession_sessionId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
,`BLSession_protectedData` varchar(1024)
,`Protein_proteinId` int(10) unsigned
,`Protein_name` varchar(255)
,`Protein_acronym` varchar(45)
,`DataCollection_dataCollectionId` int(11) unsigned
,`DataCollection_dataCollectionGroupId` int(11)
,`DataCollection_startTime` datetime
,`DataCollection_endTime` datetime
,`DataCollection_runStatus` varchar(45)
,`DataCollection_numberOfImages` int(10) unsigned
,`DataCollection_startImageNumber` int(10) unsigned
,`DataCollection_numberOfPasses` int(10) unsigned
,`DataCollection_exposureTime` float
,`DataCollection_imageDirectory` varchar(255)
,`DataCollection_wavelength` float
,`DataCollection_resolution` float
,`DataCollection_detectorDistance` float
,`DataCollection_xBeam` float
,`DataCollection_yBeam` float
,`DataCollection_comments` varchar(1024)
,`DataCollection_xtalSnapshotFullPath1` varchar(255)
,`DataCollection_xtalSnapshotFullPath2` varchar(255)
,`DataCollection_xtalSnapshotFullPath3` varchar(255)
,`DataCollection_xtalSnapshotFullPath4` varchar(255)
,`DataCollection_phiStart` float
,`DataCollection_kappaStart` float
,`DataCollection_omegaStart` float
,`DataCollection_resolutionAtCorner` float
,`DataCollection_bestWilsonPlotPath` varchar(255)
,`Workflow_workflowTitle` varchar(255)
,`Workflow_workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF','MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile')
,`Workflow_status` varchar(255)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_phasing`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary_phasing` (
`v_datacollection_summary_phasing_phasingStepId` int(10) unsigned
,`v_datacollection_summary_phasing_previousPhasingStepId` int(10) unsigned
,`v_datacollection_summary_phasing_autoProcIntegrationId` int(10) unsigned
,`v_datacollection_summary_phasing_dataCollectionId` int(11) unsigned
,`v_datacollection_summary_phasing_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
,`v_datacollection_summary_phasing_method` varchar(45)
,`v_datacollection_summary_phasing_solventContent` varchar(45)
,`v_datacollection_summary_phasing_enantiomorph` varchar(45)
,`v_datacollection_summary_phasing_lowRes` varchar(45)
,`v_datacollection_summary_phasing_highRes` varchar(45)
,`v_datacollection_summary_phasing_autoProcScalingId` int(10) unsigned
,`v_datacollection_summary_phasing_spaceGroupShortName` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_screening`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary_screening` (
`Screening_screeningId` int(10) unsigned
,`Screening_dataCollectionId` int(11) unsigned
,`Screening_dataCollectionGroupId` int(11)
,`ScreeningOutput_strategySuccess` tinyint(1)
,`ScreeningOutput_indexingSuccess` tinyint(1)
,`ScreeningOutput_rankingResolution` double
,`ScreeningOutput_mosaicityEstimated` tinyint(1)
,`ScreeningOutput_mosaicity` float
,`ScreeningOutput_totalExposureTime` double
,`ScreeningOutput_totalRotationRange` double
,`ScreeningOutput_totalNumberOfImages` int(11)
,`ScreeningOutputLattice_spaceGroup` varchar(45)
,`ScreeningOutputLattice_unitCell_a` float
,`ScreeningOutputLattice_unitCell_b` float
,`ScreeningOutputLattice_unitCell_c` float
,`ScreeningOutputLattice_unitCell_alpha` float
,`ScreeningOutputLattice_unitCell_beta` float
,`ScreeningOutputLattice_unitCell_gamma` float
,`ScreeningStrategySubWedge_exposureTime` float
,`ScreeningStrategySubWedge_transmission` float
,`ScreeningStrategySubWedge_oscillationRange` float
,`ScreeningStrategySubWedge_numberOfImages` int(10) unsigned
,`ScreeningStrategySubWedge_multiplicity` float
,`ScreeningStrategySubWedge_completeness` float
,`ScreeningStrategySubWedge_axisStart` float
,`ScreeningStrategySubWedge_axisEnd` float
,`ScreeningStrategySubWedge_rotationAxis` varchar(45)
);

-- --------------------------------------------------------

--
-- Table structure for table `v_dewar`
--

CREATE TABLE `v_dewar` (
  `proposalId` int(10) DEFAULT 0,
  `shippingId` int(10) DEFAULT 0,
  `shippingName` varchar(45) DEFAULT NULL,
  `dewarId` int(10) DEFAULT 0,
  `dewarName` varchar(45) DEFAULT NULL,
  `dewarStatus` varchar(45) DEFAULT NULL,
  `proposalCode` varchar(45) DEFAULT NULL,
  `proposalNumber` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `shippingType` varchar(45) DEFAULT NULL,
  `barCode` varchar(45) DEFAULT NULL,
  `shippingStatus` varchar(45) DEFAULT NULL,
  `beamLineName` varchar(45) DEFAULT NULL,
  `nbEvents` bigint(21) DEFAULT 0,
  `storesin` bigint(21) DEFAULT 0,
  `nbSamples` bigint(21) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_dewarBeamline`
--

CREATE TABLE `v_dewarBeamline` (
  `beamLineName` varchar(45) DEFAULT NULL,
  `COUNT(*)` bigint(21) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_dewarBeamlineByWeek`
--

CREATE TABLE `v_dewarBeamlineByWeek` (
  `Week` varchar(23) DEFAULT NULL,
  `ID14` bigint(21) DEFAULT 0,
  `ID23` bigint(21) DEFAULT 0,
  `ID29` bigint(21) DEFAULT 0,
  `BM14` bigint(21) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_dewarByWeek`
--

CREATE TABLE `v_dewarByWeek` (
  `Week` varchar(23) DEFAULT NULL,
  `Dewars Tracked` bigint(21) DEFAULT 0,
  `Dewars Non-Tracked` bigint(21) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_dewarByWeekTotal`
--

CREATE TABLE `v_dewarByWeekTotal` (
  `Week` varchar(23) DEFAULT NULL,
  `Dewars Tracked` bigint(21) DEFAULT 0,
  `Dewars Non-Tracked` bigint(21) DEFAULT 0,
  `Total` bigint(21) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_dewarList`
--

CREATE TABLE `v_dewarList` (
  `proposal` varchar(90) DEFAULT NULL,
  `shippingName` varchar(45) DEFAULT NULL,
  `dewarName` varchar(45) DEFAULT NULL,
  `barCode` varchar(45) DEFAULT NULL,
  `creationDate` varchar(10) DEFAULT NULL,
  `shippingType` varchar(45) DEFAULT NULL,
  `nbEvents` bigint(21) DEFAULT 0,
  `dewarStatus` varchar(45) DEFAULT NULL,
  `shippingStatus` varchar(45) DEFAULT NULL,
  `nbSamples` bigint(21) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_dewarProposalCode`
--

CREATE TABLE `v_dewarProposalCode` (
  `proposalCode` varchar(45) DEFAULT NULL,
  `COUNT(*)` bigint(21) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_dewarProposalCodeByWeek`
--

CREATE TABLE `v_dewarProposalCodeByWeek` (
  `Week` varchar(23) DEFAULT NULL,
  `MX` bigint(21) DEFAULT 0,
  `FX` bigint(21) DEFAULT 0,
  `BM14U` bigint(21) DEFAULT 0,
  `BM161` bigint(21) DEFAULT 0,
  `BM162` bigint(21) DEFAULT 0,
  `Others` bigint(21) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewar_summary`
-- (See below for the actual view)
--
CREATE TABLE `v_dewar_summary` (
`shippingName` varchar(45)
,`deliveryAgent_agentName` varchar(45)
,`deliveryAgent_shippingDate` date
,`deliveryAgent_deliveryDate` date
,`deliveryAgent_agentCode` varchar(45)
,`deliveryAgent_flightCode` varchar(45)
,`shippingStatus` varchar(45)
,`bltimeStamp` datetime
,`laboratoryId` int(10) unsigned
,`isStorageShipping` tinyint(1)
,`creationDate` datetime
,`Shipping_comments` varchar(255)
,`sendingLabContactId` int(10) unsigned
,`returnLabContactId` int(10) unsigned
,`returnCourier` varchar(45)
,`dateOfShippingToUser` datetime
,`shippingType` varchar(45)
,`dewarId` int(10) unsigned
,`shippingId` int(10) unsigned
,`dewarCode` varchar(45)
,`comments` tinytext
,`storageLocation` varchar(45)
,`dewarStatus` varchar(45)
,`isStorageDewar` tinyint(1)
,`barCode` varchar(45)
,`firstExperimentId` int(10) unsigned
,`customsValue` int(11) unsigned
,`transportValue` int(11) unsigned
,`trackingNumberToSynchrotron` varchar(30)
,`trackingNumberFromSynchrotron` varchar(30)
,`type` enum('Dewar','Toolbox')
,`isReimbursed` tinyint(1)
,`sessionId` int(10) unsigned
,`beamlineName` varchar(45)
,`sessionStartDate` datetime
,`sessionEndDate` datetime
,`beamLineOperator` varchar(255)
,`nbReimbDewars` int(10)
,`proposalId` int(10) unsigned
,`containerId` int(10) unsigned
,`containerType` varchar(20)
,`capacity` int(10) unsigned
,`beamlineLocation` varchar(20)
,`sampleChangerLocation` varchar(20)
,`containerStatus` varchar(45)
,`containerCode` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_em_classification`
-- (See below for the actual view)
--
CREATE TABLE `v_em_classification` (
`proposalId` int(10) unsigned
,`sessionId` int(10) unsigned
,`imageDirectory` varchar(255)
,`particlePickerId` int(10) unsigned
,`numberOfParticles` int(10) unsigned
,`particleClassificationGroupId` int(10) unsigned
,`particleClassificationId` int(10) unsigned
,`classNumber` int(10) unsigned
,`classImageFullPath` varchar(255)
,`particlesPerClass` int(10) unsigned
,`classDistribution` float
,`rotationAccuracy` float unsigned
,`translationAccuracy` float
,`estimatedResolution` float
,`overallFourierCompleteness` float
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_energyScan`
-- (See below for the actual view)
--
CREATE TABLE `v_energyScan` (
`energyScanId` int(10) unsigned
,`sessionId` int(10) unsigned
,`blSampleId` int(10) unsigned
,`fluorescenceDetector` varchar(255)
,`scanFileFullPath` varchar(255)
,`choochFileFullPath` varchar(255)
,`jpegChoochFileFullPath` varchar(255)
,`element` varchar(45)
,`startEnergy` float
,`endEnergy` float
,`transmissionFactor` float
,`exposureTime` float
,`synchrotronCurrent` float
,`temperature` float
,`peakEnergy` float
,`peakFPrime` float
,`peakFDoublePrime` float
,`inflectionEnergy` float
,`inflectionFPrime` float
,`inflectionFDoublePrime` float
,`xrayDose` float
,`startTime` datetime
,`endTime` datetime
,`edgeEnergy` varchar(255)
,`filename` varchar(255)
,`beamSizeVertical` float
,`beamSizeHorizontal` float
,`crystalClass` varchar(20)
,`comments` varchar(1024)
,`flux` double
,`flux_end` double
,`BLSample_sampleId` int(10) unsigned
,`name` varchar(100)
,`code` varchar(45)
,`acronym` varchar(45)
,`BLSession_proposalId` int(10) unsigned
);

-- --------------------------------------------------------

--
-- Table structure for table `v_hour`
--

CREATE TABLE `v_hour` (
  `num` varchar(18) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_Log4Stat`
--

CREATE TABLE `v_Log4Stat` (
  `id` int(11) DEFAULT 0,
  `priority` varchar(15) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_logonByHour`
--

CREATE TABLE `v_logonByHour` (
  `Hour` varchar(7) DEFAULT NULL,
  `Distinct logins` bigint(21) DEFAULT 0,
  `Total logins` bigint(22) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_logonByHour2`
--

CREATE TABLE `v_logonByHour2` (
  `Hour` varchar(7) DEFAULT NULL,
  `Distinct logins` bigint(21) DEFAULT 0,
  `Total logins` bigint(22) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_logonByMonthDay`
--

CREATE TABLE `v_logonByMonthDay` (
  `Day` varchar(5) DEFAULT NULL,
  `Distinct logins` bigint(21) DEFAULT 0,
  `Total logins` bigint(22) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_logonByMonthDay2`
--

CREATE TABLE `v_logonByMonthDay2` (
  `Day` varchar(5) DEFAULT NULL,
  `Distinct logins` bigint(21) DEFAULT 0,
  `Total logins` bigint(22) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_logonByWeek`
--

CREATE TABLE `v_logonByWeek` (
  `Week` varchar(23) DEFAULT NULL,
  `Distinct logins` bigint(21) DEFAULT 0,
  `Total logins` bigint(22) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_logonByWeek2`
--

CREATE TABLE `v_logonByWeek2` (
  `Week` varchar(23) DEFAULT NULL,
  `Distinct logins` bigint(21) DEFAULT 0,
  `Total logins` bigint(22) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_logonByWeekDay`
--

CREATE TABLE `v_logonByWeekDay` (
  `Day` varchar(64) DEFAULT NULL,
  `Distinct logins` bigint(21) DEFAULT 0,
  `Total logins` bigint(22) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_logonByWeekDay2`
--

CREATE TABLE `v_logonByWeekDay2` (
  `Day` varchar(64) DEFAULT NULL,
  `Distinct logins` bigint(21) DEFAULT 0,
  `Total logins` bigint(22) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_monthDay`
--

CREATE TABLE `v_monthDay` (
  `num` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_mx_autoprocessing_stats`
-- (See below for the actual view)
--
CREATE TABLE `v_mx_autoprocessing_stats` (
`autoProcScalingStatisticsId` int(10) unsigned
,`autoProcScalingId` int(10) unsigned
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitLow` float
,`resolutionLimitHigh` float
,`rMerge` float
,`rMeasWithinIPlusIMinus` float
,`rMeasAllIPlusIMinus` float
,`rPimWithinIPlusIMinus` float
,`rPimAllIPlusIMinus` float
,`fractionalPartialBias` float
,`nTotalObservations` int(10)
,`nTotalUniqueObservations` int(10)
,`meanIOverSigI` float
,`completeness` float
,`multiplicity` float
,`anomalousCompleteness` float
,`anomalousMultiplicity` float
,`recordTimeStamp` datetime
,`anomalous` tinyint(1)
,`ccHalf` float
,`ccAno` float
,`sigAno` varchar(45)
,`ISA` varchar(45)
,`dataCollectionId` int(11) unsigned
,`strategySubWedgeOrigId` int(10) unsigned
,`detectorId` int(11)
,`blSubSampleId` int(11) unsigned
,`dataCollectionNumber` int(10) unsigned
,`startTime` datetime
,`endTime` datetime
,`sessionId` int(10) unsigned
,`proposalId` int(10) unsigned
,`beamLineName` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_mx_experiment_stats`
-- (See below for the actual view)
--
CREATE TABLE `v_mx_experiment_stats` (
`startTime` datetime
,`Images` int(10) unsigned
,`Transmission` float
,`Res. (corner)` float
,`En. (Wave.)` float
,`Omega start (total)` float
,`Exposure Time` float
,`Flux` double
,`Flux End` double
,`Detector Distance` float
,`X Beam` float
,`Y Beam` float
,`Kappa` float
,`Phi` float
,`Axis Start` float
,`Axis End` float
,`Axis Range` float
,`Beam Size X` float
,`Beam Size Y` float
,`beamLineName` varchar(45)
,`comments` varchar(1024)
,`proposalNumber` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_mx_sample`
-- (See below for the actual view)
--
CREATE TABLE `v_mx_sample` (
`BLSample_blSampleId` int(10) unsigned
,`BLSample_diffractionPlanId` int(10) unsigned
,`BLSample_crystalId` int(10) unsigned
,`BLSample_containerId` int(10) unsigned
,`BLSample_name` varchar(100)
,`BLSample_code` varchar(45)
,`BLSample_location` varchar(45)
,`BLSample_holderLength` double
,`BLSample_loopLength` double
,`BLSample_loopType` varchar(45)
,`BLSample_wireWidth` double
,`BLSample_comments` varchar(1024)
,`BLSample_completionStage` varchar(45)
,`BLSample_structureStage` varchar(45)
,`BLSample_publicationStage` varchar(45)
,`BLSample_publicationComments` varchar(255)
,`BLSample_blSampleStatus` varchar(20)
,`BLSample_isInSampleChanger` tinyint(1)
,`BLSample_lastKnownCenteringPosition` varchar(255)
,`BLSample_recordTimeStamp` timestamp
,`BLSample_SMILES` varchar(400)
,`Protein_proteinId` int(10) unsigned
,`Protein_name` varchar(255)
,`Protein_acronym` varchar(45)
,`Protein_proteinType` varchar(45)
,`Protein_proposalId` int(10) unsigned
,`Person_personId` int(10) unsigned
,`Person_familyName` varchar(45)
,`Person_givenName` varchar(45)
,`Person_emailAddress` varchar(60)
,`Container_containerId` int(10) unsigned
,`Container_code` varchar(45)
,`Container_containerType` varchar(20)
,`Container_containerStatus` varchar(45)
,`Container_beamlineLocation` varchar(20)
,`Container_sampleChangerLocation` varchar(20)
,`Dewar_code` varchar(45)
,`Dewar_dewarId` int(10) unsigned
,`Dewar_storageLocation` varchar(45)
,`Dewar_dewarStatus` varchar(45)
,`Dewar_barCode` varchar(45)
,`Shipping_shippingId` int(10) unsigned
,`sessionId` int(10) unsigned
,`BLSession_startDate` datetime
,`BLSession_beamLineName` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_phasing`
-- (See below for the actual view)
--
CREATE TABLE `v_phasing` (
`BLSample_blSampleId` int(10) unsigned
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`AutoProcIntegration_autoProcProgramId` int(10) unsigned
,`AutoProcIntegration_startImageNumber` int(10) unsigned
,`AutoProcIntegration_endImageNumber` int(10) unsigned
,`AutoProcIntegration_refinedDetectorDistance` float
,`AutoProcIntegration_refinedXBeam` float
,`AutoProcIntegration_refinedYBeam` float
,`AutoProcIntegration_rotationAxisX` float
,`AutoProcIntegration_rotationAxisY` float
,`AutoProcIntegration_rotationAxisZ` float
,`AutoProcIntegration_beamVectorX` float
,`AutoProcIntegration_beamVectorY` float
,`AutoProcIntegration_beamVectorZ` float
,`AutoProcIntegration_cell_a` float
,`AutoProcIntegration_cell_b` float
,`AutoProcIntegration_cell_c` float
,`AutoProcIntegration_cell_alpha` float
,`AutoProcIntegration_cell_beta` float
,`AutoProcIntegration_cell_gamma` float
,`AutoProcIntegration_recordTimeStamp` datetime
,`AutoProcIntegration_anomalous` tinyint(1)
,`SpaceGroup_spaceGroupId` int(10) unsigned
,`SpaceGroup_geometryClassnameId` int(11) unsigned
,`SpaceGroup_spaceGroupNumber` int(10) unsigned
,`SpaceGroup_spaceGroupShortName` varchar(45)
,`SpaceGroup_spaceGroupName` varchar(45)
,`SpaceGroup_bravaisLattice` varchar(45)
,`SpaceGroup_bravaisLatticeName` varchar(45)
,`SpaceGroup_pointGroup` varchar(45)
,`SpaceGroup_MX_used` tinyint(1)
,`PhasingStep_phasingStepId` int(10) unsigned
,`PhasingStep_previousPhasingStepId` int(10) unsigned
,`PhasingStep_programRunId` int(10) unsigned
,`PhasingStep_spaceGroupId` int(10) unsigned
,`PhasingStep_autoProcScalingId` int(10) unsigned
,`PhasingStep_phasingAnalysisId` int(10) unsigned
,`PhasingStep_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
,`PhasingStep_method` varchar(45)
,`PhasingStep_solventContent` varchar(45)
,`PhasingStep_enantiomorph` varchar(45)
,`PhasingStep_lowRes` varchar(45)
,`PhasingStep_highRes` varchar(45)
,`PhasingStep_recordTimeStamp` timestamp
,`DataCollection_dataCollectionId` int(11) unsigned
,`DataCollection_dataCollectionGroupId` int(11)
,`DataCollection_strategySubWedgeOrigId` int(10) unsigned
,`DataCollection_detectorId` int(11)
,`DataCollection_blSubSampleId` int(11) unsigned
,`DataCollection_dataCollectionNumber` int(10) unsigned
,`DataCollection_startTime` datetime
,`DataCollection_endTime` datetime
,`DataCollection_runStatus` varchar(45)
,`DataCollection_axisStart` float
,`DataCollection_axisEnd` float
,`DataCollection_axisRange` float
,`DataCollection_overlap` float
,`DataCollection_numberOfImages` int(10) unsigned
,`DataCollection_startImageNumber` int(10) unsigned
,`DataCollection_numberOfPasses` int(10) unsigned
,`DataCollection_exposureTime` float
,`DataCollection_imageDirectory` varchar(255)
,`DataCollection_imagePrefix` varchar(100)
,`DataCollection_imageSuffix` varchar(45)
,`DataCollection_fileTemplate` varchar(255)
,`DataCollection_wavelength` float
,`DataCollection_resolution` float
,`DataCollection_detectorDistance` float
,`DataCollection_xBeam` float
,`DataCollection_yBeam` float
,`DataCollection_xBeamPix` float
,`DataCollection_yBeamPix` float
,`DataCollection_comments` varchar(1024)
,`DataCollection_printableForReport` tinyint(1) unsigned
,`DataCollection_slitGapVertical` float
,`DataCollection_slitGapHorizontal` float
,`DataCollection_transmission` float
,`DataCollection_synchrotronMode` varchar(20)
,`DataCollection_xtalSnapshotFullPath1` varchar(255)
,`DataCollection_xtalSnapshotFullPath2` varchar(255)
,`DataCollection_xtalSnapshotFullPath3` varchar(255)
,`DataCollection_xtalSnapshotFullPath4` varchar(255)
,`DataCollection_rotationAxis` enum('Omega','Kappa','Phi')
,`DataCollection_phiStart` float
,`DataCollection_kappaStart` float
,`DataCollection_omegaStart` float
,`DataCollection_resolutionAtCorner` float
,`DataCollection_detector2Theta` float
,`DataCollection_undulatorGap1` float
,`DataCollection_undulatorGap2` float
,`DataCollection_undulatorGap3` float
,`DataCollection_beamSizeAtSampleX` float
,`DataCollection_beamSizeAtSampleY` float
,`DataCollection_centeringMethod` varchar(255)
,`DataCollection_averageTemperature` float
,`DataCollection_actualCenteringPosition` varchar(255)
,`DataCollection_beamShape` varchar(45)
,`DataCollection_flux` double
,`DataCollection_flux_end` double
,`DataCollection_totalAbsorbedDose` double
,`DataCollection_bestWilsonPlotPath` varchar(255)
,`DataCollection_imageQualityIndicatorsPlotPath` varchar(512)
,`DataCollection_imageQualityIndicatorsCSVPath` varchar(512)
,`PhasingProgramRun_phasingProgramRunId` int(11) unsigned
,`PhasingProgramRun_phasingCommandLine` varchar(255)
,`PhasingProgramRun_phasingPrograms` varchar(255)
,`PhasingProgramRun_phasingStatus` tinyint(1)
,`PhasingProgramRun_phasingMessage` varchar(255)
,`PhasingProgramRun_phasingStartTime` datetime
,`PhasingProgramRun_phasingEndTime` datetime
,`PhasingProgramRun_phasingEnvironment` varchar(255)
,`PhasingProgramRun_phasingDirectory` varchar(255)
,`PhasingProgramRun_recordTimeStamp` timestamp
,`Protein_proteinId` int(10) unsigned
,`BLSession_sessionId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
,`PhasingStatistics_phasingStatisticsId` int(11) unsigned
,`PhasingStatistics_metric` enum('Rcullis','Average Fragment Length','Chain Count','Residues Count','CC','PhasingPower','FOM','<d"/sig>','Best CC','CC(1/2)','Weak CC','CFOM','Pseudo_free_CC','CC of partial model','Start R-work','Start R-free','Final R-work','Final R-free')
,`PhasingStatistics_statisticsValue` double
);

-- --------------------------------------------------------

--
-- Table structure for table `v_run`
--

CREATE TABLE `v_run` (
  `runId` int(11) NOT NULL,
  `run` varchar(7) NOT NULL DEFAULT '',
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_sample`
--

CREATE TABLE `v_sample` (
  `proposalId` int(10) DEFAULT 0,
  `shippingId` int(10) DEFAULT 0,
  `dewarId` int(10) DEFAULT 0,
  `containerId` int(10) DEFAULT 0,
  `blSampleId` int(10) DEFAULT 0,
  `proposalCode` varchar(45) DEFAULT NULL,
  `proposalNumber` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `shippingType` varchar(45) DEFAULT NULL,
  `barCode` varchar(45) DEFAULT NULL,
  `shippingStatus` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_sampleByWeek`
--

CREATE TABLE `v_sampleByWeek` (
  `Week` varchar(23) DEFAULT NULL,
  `Samples` bigint(21) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_session`
-- (See below for the actual view)
--
CREATE TABLE `v_session` (
`sessionId` int(10) unsigned
,`expSessionPk` int(11) unsigned
,`beamLineSetupId` int(10) unsigned
,`proposalId` int(10) unsigned
,`projectCode` varchar(45)
,`BLSession_startDate` datetime
,`BLSession_endDate` datetime
,`beamLineName` varchar(45)
,`scheduled` tinyint(1)
,`nbShifts` int(10) unsigned
,`comments` varchar(2000)
,`beamLineOperator` varchar(255)
,`visit_number` int(10) unsigned
,`bltimeStamp` timestamp
,`usedFlag` tinyint(1)
,`sessionTitle` varchar(255)
,`structureDeterminations` float
,`dewarTransport` float
,`databackupFrance` float
,`databackupEurope` float
,`operatorSiteNumber` varchar(10)
,`BLSession_lastUpdate` timestamp
,`BLSession_protectedData` varchar(1024)
,`Proposal_title` varchar(200)
,`Proposal_proposalCode` varchar(45)
,`Proposal_ProposalNumber` varchar(45)
,`Proposal_ProposalType` varchar(2)
,`Person_personId` int(10) unsigned
,`Person_familyName` varchar(45)
,`Person_givenName` varchar(45)
,`Person_emailAddress` varchar(60)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_tracking_shipment_history`
-- (See below for the actual view)
--
CREATE TABLE `v_tracking_shipment_history` (
`Dewar_dewarId` int(10) unsigned
,`Dewar_code` varchar(45)
,`Dewar_comments` tinytext
,`Dewar_dewarStatus` varchar(45)
,`Dewar_barCode` varchar(45)
,`Dewar_firstExperimentId` int(10) unsigned
,`Dewar_trackingNumberToSynchrotron` varchar(30)
,`Dewar_trackingNumberFromSynchrotron` varchar(30)
,`Dewar_type` enum('Dewar','Toolbox')
,`Shipping_shippingId` int(10) unsigned
,`Shipping_proposalId` int(10) unsigned
,`Shipping_shippingName` varchar(45)
,`deliveryAgent_agentName` varchar(45)
,`Shipping_deliveryAgent_shippingDate` date
,`Shipping_deliveryAgent_deliveryDate` date
,`Shipping_shippingStatus` varchar(45)
,`Shipping_returnCourier` varchar(45)
,`Shipping_dateOfShippingToUser` datetime
,`DewarTransportHistory_DewarTransportHistoryId` int(10) unsigned
,`DewarTransportHistory_dewarStatus` varchar(45)
,`DewarTransportHistory_storageLocation` varchar(45)
,`DewarTransportHistory_arrivalDate` datetime
);

-- --------------------------------------------------------

--
-- Table structure for table `v_week`
--

CREATE TABLE `v_week` (
  `num` varchar(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `v_weekDay`
--

CREATE TABLE `v_weekDay` (
  `day` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_xfeFluorescenceSpectrum`
-- (See below for the actual view)
--
CREATE TABLE `v_xfeFluorescenceSpectrum` (
`xfeFluorescenceSpectrumId` int(10) unsigned
,`sessionId` int(10) unsigned
,`blSampleId` int(10) unsigned
,`fittedDataFileFullPath` varchar(255)
,`scanFileFullPath` varchar(255)
,`jpegScanFileFullPath` varchar(255)
,`startTime` datetime
,`endTime` datetime
,`filename` varchar(255)
,`energy` float
,`exposureTime` float
,`beamTransmission` float
,`annotatedPymcaXfeSpectrum` varchar(255)
,`beamSizeVertical` float
,`beamSizeHorizontal` float
,`crystalClass` varchar(20)
,`comments` varchar(1024)
,`flux` double
,`flux_end` double
,`workingDirectory` varchar(512)
,`BLSample_sampleId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
);

-- --------------------------------------------------------

--
-- Table structure for table `Workflow`
--

CREATE TABLE `Workflow` (
  `workflowId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `workflowTitle` varchar(255) DEFAULT NULL,
  `workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF','MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile') DEFAULT NULL,
  `workflowTypeId` int(11) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `resultFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowDehydration`
--

CREATE TABLE `WorkflowDehydration` (
  `workflowDehydrationId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `workflowId` int(11) UNSIGNED NOT NULL COMMENT 'Related workflow',
  `dataFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowMesh`
--

CREATE TABLE `WorkflowMesh` (
  `workflowMeshId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `workflowId` int(11) UNSIGNED NOT NULL COMMENT 'Related workflow',
  `bestPositionId` int(11) UNSIGNED DEFAULT NULL,
  `bestImageId` int(12) UNSIGNED DEFAULT NULL,
  `value1` double DEFAULT NULL,
  `value2` double DEFAULT NULL,
  `value3` double DEFAULT NULL COMMENT 'N value',
  `value4` double DEFAULT NULL,
  `cartographyPath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowStep`
--

CREATE TABLE `WorkflowStep` (
  `workflowStepId` int(11) NOT NULL,
  `workflowId` int(11) UNSIGNED NOT NULL,
  `workflowStepType` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `folderPath` varchar(1024) DEFAULT NULL,
  `imageResultFilePath` varchar(1024) DEFAULT NULL,
  `htmlResultFilePath` varchar(1024) DEFAULT NULL,
  `resultFilePath` varchar(1024) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  `crystalSizeX` varchar(45) DEFAULT NULL,
  `crystalSizeY` varchar(45) DEFAULT NULL,
  `crystalSizeZ` varchar(45) DEFAULT NULL,
  `maxDozorScore` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowType`
--

CREATE TABLE `WorkflowType` (
  `workflowTypeId` int(11) NOT NULL,
  `workflowTypeName` varchar(45) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `XFEFluorescenceSpectrum`
--

CREATE TABLE `XFEFluorescenceSpectrum` (
  `xfeFluorescenceSpectrumId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `blSampleId` int(10) UNSIGNED DEFAULT NULL,
  `fittedDataFileFullPath` varchar(255) DEFAULT NULL,
  `scanFileFullPath` varchar(255) DEFAULT NULL,
  `jpegScanFileFullPath` varchar(255) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `energy` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `axisPosition` float DEFAULT NULL,
  `beamTransmission` float DEFAULT NULL,
  `annotatedPymcaXfeSpectrum` varchar(255) DEFAULT NULL,
  `beamSizeVertical` float DEFAULT NULL,
  `beamSizeHorizontal` float DEFAULT NULL,
  `crystalClass` varchar(20) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `flux` double DEFAULT NULL COMMENT 'flux measured before the xrfSpectra',
  `flux_end` double DEFAULT NULL COMMENT 'flux measured after the xrfSpectra',
  `workingDirectory` varchar(512) DEFAULT NULL,
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `XRFFluorescenceMapping`
--

CREATE TABLE `XRFFluorescenceMapping` (
  `xrfFluorescenceMappingId` int(11) UNSIGNED NOT NULL,
  `xrfFluorescenceMappingROIId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `imageNumber` int(10) UNSIGNED NOT NULL,
  `counts` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `XRFFluorescenceMappingROI`
--

CREATE TABLE `XRFFluorescenceMappingROI` (
  `xrfFluorescenceMappingROIId` int(11) UNSIGNED NOT NULL,
  `startEnergy` float NOT NULL,
  `endEnergy` float NOT NULL,
  `element` varchar(2) DEFAULT NULL,
  `edge` varchar(2) DEFAULT NULL COMMENT 'In future may be changed to enum(K, L)',
  `r` tinyint(3) UNSIGNED DEFAULT NULL COMMENT 'R colour component',
  `g` tinyint(3) UNSIGNED DEFAULT NULL COMMENT 'G colour component',
  `b` tinyint(3) UNSIGNED DEFAULT NULL COMMENT 'B colour component'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection`
--
DROP TABLE IF EXISTS `v_datacollection`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection`  AS SELECT `DataCollection`.`dataCollectionId` AS `dataCollectionId`, `DataCollection`.`dataCollectionGroupId` AS `dataCollectionGroupId`, `DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`, `DataCollection`.`detectorId` AS `detectorId`, `DataCollection`.`blSubSampleId` AS `blSubSampleId`, `DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`, `DataCollection`.`startTime` AS `startTime`, `DataCollection`.`endTime` AS `endTime`, `DataCollection`.`runStatus` AS `runStatus`, `DataCollection`.`axisStart` AS `axisStart`, `DataCollection`.`axisEnd` AS `axisEnd`, `DataCollection`.`axisRange` AS `axisRange`, `DataCollection`.`overlap` AS `overlap`, `DataCollection`.`numberOfImages` AS `numberOfImages`, `DataCollection`.`startImageNumber` AS `startImageNumber`, `DataCollection`.`numberOfPasses` AS `numberOfPasses`, `DataCollection`.`exposureTime` AS `exposureTime`, `DataCollection`.`imageDirectory` AS `imageDirectory`, `DataCollection`.`imagePrefix` AS `imagePrefix`, `DataCollection`.`imageSuffix` AS `imageSuffix`, `DataCollection`.`fileTemplate` AS `fileTemplate`, `DataCollection`.`wavelength` AS `wavelength`, `DataCollection`.`resolution` AS `resolution`, `DataCollection`.`detectorDistance` AS `detectorDistance`, `DataCollection`.`xBeam` AS `xBeam`, `DataCollection`.`yBeam` AS `yBeam`, `DataCollection`.`xBeamPix` AS `xBeamPix`, `DataCollection`.`yBeamPix` AS `yBeamPix`, `DataCollection`.`comments` AS `comments`, `DataCollection`.`printableForReport` AS `printableForReport`, `DataCollection`.`slitGapVertical` AS `slitGapVertical`, `DataCollection`.`slitGapHorizontal` AS `slitGapHorizontal`, `DataCollection`.`transmission` AS `transmission`, `DataCollection`.`synchrotronMode` AS `synchrotronMode`, `DataCollection`.`xtalSnapshotFullPath1` AS `xtalSnapshotFullPath1`, `DataCollection`.`xtalSnapshotFullPath2` AS `xtalSnapshotFullPath2`, `DataCollection`.`xtalSnapshotFullPath3` AS `xtalSnapshotFullPath3`, `DataCollection`.`xtalSnapshotFullPath4` AS `xtalSnapshotFullPath4`, `DataCollection`.`rotationAxis` AS `rotationAxis`, `DataCollection`.`phiStart` AS `phiStart`, `DataCollection`.`kappaStart` AS `kappaStart`, `DataCollection`.`omegaStart` AS `omegaStart`, `DataCollection`.`resolutionAtCorner` AS `resolutionAtCorner`, `DataCollection`.`detector2Theta` AS `detector2Theta`, `DataCollection`.`undulatorGap1` AS `undulatorGap1`, `DataCollection`.`undulatorGap2` AS `undulatorGap2`, `DataCollection`.`undulatorGap3` AS `undulatorGap3`, `DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`, `DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`, `DataCollection`.`centeringMethod` AS `centeringMethod`, `DataCollection`.`averageTemperature` AS `averageTemperature`, `DataCollection`.`actualCenteringPosition` AS `actualCenteringPosition`, `DataCollection`.`beamShape` AS `beamShape`, `DataCollection`.`flux` AS `flux`, `DataCollection`.`flux_end` AS `flux_end`, `DataCollection`.`totalAbsorbedDose` AS `totalAbsorbedDose`, `DataCollection`.`bestWilsonPlotPath` AS `bestWilsonPlotPath`, `DataCollection`.`imageQualityIndicatorsPlotPath` AS `imageQualityIndicatorsPlotPath`, `DataCollection`.`imageQualityIndicatorsCSVPath` AS `imageQualityIndicatorsCSVPath`, `BLSession`.`sessionId` AS `sessionId`, `BLSession`.`proposalId` AS `proposalId`, `DataCollectionGroup`.`workflowId` AS `workflowId`, `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`, `v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`, `v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`, `v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`, `v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`, `v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`, `v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`, `v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`, `v_datacollection_summary_autoprocintegration`.`anomalous` AS `anomalous`, `v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`, `v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`, `v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`, `v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`, `v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`, `v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`, `v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`, `v_datacollection_summary_autoprocintegration`.`ccHalf` AS `ccHalf`, `v_datacollection_summary_autoprocintegration`.`meanIOverSigI` AS `meanIOverSigI`, `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`, `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`, `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`, `v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`, `ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution` FROM ((((((`DataCollection` left join `DataCollectionGroup` on(`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)) left join `Screening` on(`Screening`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)) left join `ScreeningOutput` on(`Screening`.`screeningId` = `ScreeningOutput`.`screeningId`)) left join `Workflow` on(`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)) left join `BLSession` on(`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)) left join `v_datacollection_summary_autoprocintegration` on(`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_autoprocintegration`
--
DROP TABLE IF EXISTS `v_datacollection_autoprocintegration`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_autoprocintegration`  AS SELECT `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`, `AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`, `AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`, `AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`, `AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`, `AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`, `AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`, `AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`, `AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`, `AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`, `AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`, `AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`, `AutoProcProgram`.`processingPrograms` AS `v_datacollection_processingPrograms`, `AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`, `AutoProcProgram`.`processingStatus` AS `v_datacollection_processingStatus`, `AutoProcProgram`.`processingStartTime` AS `v_datacollection_processingStartTime`, `AutoProcProgram`.`processingEndTime` AS `v_datacollection_processingEndTime`, `BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`, `BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId`, `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`, `AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`, `PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`, `SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`, `Protein`.`proteinId` AS `Protein_proteinId`, `Protein`.`acronym` AS `Protein_acronym`, `BLSample`.`name` AS `BLSample_name`, `DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`, `DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix` FROM (((((((((((((`AutoProcIntegration` left join `DataCollection` on(`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`)) left join `DataCollectionGroup` on(`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)) left join `BLSession` on(`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)) left join `AutoProcScaling_has_Int` on(`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`)) left join `AutoProcScaling` on(`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)) left join `AutoProcProgram` on(`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)) left join `Phasing_has_Scaling` on(`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)) left join `PhasingStep` on(`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`)) left join `SpaceGroup` on(`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)) left join `AutoProc` on(`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)) left join `BLSample` on(`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`)) left join `Crystal` on(`Crystal`.`crystalId` = `BLSample`.`crystalId`)) left join `Protein` on(`Protein`.`proteinId` = `Crystal`.`proteinId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary`
--
DROP TABLE IF EXISTS `v_datacollection_summary`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary`  AS SELECT `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`, `DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`, `DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`, `DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`, `DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`, `DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`, `DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`, `DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`, `DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`, `DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`, `DataCollectionGroup`.`crystalClass` AS `DataCollectionGroup_crystalClass`, `BLSample`.`blSampleId` AS `BLSample_blSampleId`, `BLSample`.`crystalId` AS `BLSample_crystalId`, `BLSample`.`name` AS `BLSample_name`, `BLSample`.`code` AS `BLSample_code`, `BLSample`.`location` AS `BLSample_location`, `BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`, `BLSample`.`comments` AS `BLSample_comments`, `Container`.`containerId` AS `Container_containerId`, `BLSession`.`sessionId` AS `BLSession_sessionId`, `BLSession`.`proposalId` AS `BLSession_proposalId`, `BLSession`.`beamLineName` AS `BLSession_beamLineName`, `BLSession`.`protectedData` AS `BLSession_protectedData`, `Dewar`.`dewarId` AS `Dewar_dewarId`, `Dewar`.`code` AS `Dewar_code`, `Dewar`.`storageLocation` AS `Dewar_storageLocation`, `Container`.`containerType` AS `Container_containerType`, `Container`.`code` AS `Container_code`, `Container`.`capacity` AS `Container_capacity`, `Container`.`beamlineLocation` AS `Container_beamlineLocation`, `Container`.`sampleChangerLocation` AS `Container_sampleChangerLocation`, `Protein`.`proteinId` AS `Protein_proteinId`, `Protein`.`name` AS `Protein_name`, `Protein`.`acronym` AS `Protein_acronym`, `DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`, `DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`, `DataCollection`.`startTime` AS `DataCollection_startTime`, `DataCollection`.`endTime` AS `DataCollection_endTime`, `DataCollection`.`runStatus` AS `DataCollection_runStatus`, `DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`, `DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`, `DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`, `DataCollection`.`exposureTime` AS `DataCollection_exposureTime`, `DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`, `DataCollection`.`wavelength` AS `DataCollection_wavelength`, `DataCollection`.`resolution` AS `DataCollection_resolution`, `DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`, `DataCollection`.`xBeam` AS `DataCollection_xBeam`, `DataCollection`.`transmission` AS `transmission`, `DataCollection`.`yBeam` AS `DataCollection_yBeam`, `DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`, `DataCollection`.`comments` AS `DataCollection_comments`, `DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`, `DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`, `DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`, `DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`, `DataCollection`.`phiStart` AS `DataCollection_phiStart`, `DataCollection`.`kappaStart` AS `DataCollection_kappaStart`, `DataCollection`.`omegaStart` AS `DataCollection_omegaStart`, `DataCollection`.`flux` AS `DataCollection_flux`, `DataCollection`.`flux_end` AS `DataCollection_flux_end`, `DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`, `DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`, `DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`, `DataCollection`.`axisRange` AS `DataCollection_axisRange`, `DataCollection`.`axisStart` AS `DataCollection_axisStart`, `DataCollection`.`axisEnd` AS `DataCollection_axisEnd`, `DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`, `DataCollection`.`undulatorGap1` AS `DataCollection_undulatorGap1`, `DataCollection`.`undulatorGap2` AS `DataCollection_undulatorGap2`, `DataCollection`.`undulatorGap3` AS `DataCollection_undulatorGap3`, `DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`, `DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`, `DataCollection`.`slitGapVertical` AS `DataCollection_slitGapVertical`, `DataCollection`.`slitGapHorizontal` AS `DataCollection_slitGapHorizontal`, `DataCollection`.`beamShape` AS `DataCollection_beamShape`, `DataCollection`.`voltage` AS `DataCollection_voltage`, `DataCollection`.`xBeamPix` AS `DataCollection_xBeamPix`, `Workflow`.`workflowTitle` AS `Workflow_workflowTitle`, `Workflow`.`workflowType` AS `Workflow_workflowType`, `Workflow`.`status` AS `Workflow_status`, `Workflow`.`workflowId` AS `Workflow_workflowId`, `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`, `v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`, `v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`, `v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`, `v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`, `v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`, `v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`, `v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`, `v_datacollection_summary_autoprocintegration`.`anomalous` AS `anomalous`, `v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`, `v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`, `v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`, `v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`, `v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`, `v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`, `v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`, `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`, `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`, `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`, `v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`, `v_datacollection_summary_screening`.`Screening_screeningId` AS `Screening_screeningId`, `v_datacollection_summary_screening`.`Screening_dataCollectionId` AS `Screening_dataCollectionId`, `v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` AS `Screening_dataCollectionGroupId`, `v_datacollection_summary_screening`.`ScreeningOutput_strategySuccess` AS `ScreeningOutput_strategySuccess`, `v_datacollection_summary_screening`.`ScreeningOutput_indexingSuccess` AS `ScreeningOutput_indexingSuccess`, `v_datacollection_summary_screening`.`ScreeningOutput_rankingResolution` AS `ScreeningOutput_rankingResolution`, `v_datacollection_summary_screening`.`ScreeningOutput_mosaicity` AS `ScreeningOutput_mosaicity`, `v_datacollection_summary_screening`.`ScreeningOutputLattice_spaceGroup` AS `ScreeningOutputLattice_spaceGroup`, `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_a` AS `ScreeningOutputLattice_unitCell_a`, `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_b` AS `ScreeningOutputLattice_unitCell_b`, `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_c` AS `ScreeningOutputLattice_unitCell_c`, `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`, `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`, `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`, `v_datacollection_summary_screening`.`ScreeningOutput_totalExposureTime` AS `ScreeningOutput_totalExposureTime`, `v_datacollection_summary_screening`.`ScreeningOutput_totalRotationRange` AS `ScreeningOutput_totalRotationRange`, `v_datacollection_summary_screening`.`ScreeningOutput_totalNumberOfImages` AS `ScreeningOutput_totalNumberOfImages`, `v_datacollection_summary_screening`.`ScreeningStrategySubWedge_exposureTime` AS `ScreeningStrategySubWedge_exposureTime`, `v_datacollection_summary_screening`.`ScreeningStrategySubWedge_transmission` AS `ScreeningStrategySubWedge_transmission`, `v_datacollection_summary_screening`.`ScreeningStrategySubWedge_oscillationRange` AS `ScreeningStrategySubWedge_oscillationRange`, `v_datacollection_summary_screening`.`ScreeningStrategySubWedge_numberOfImages` AS `ScreeningStrategySubWedge_numberOfImages`, `v_datacollection_summary_screening`.`ScreeningStrategySubWedge_multiplicity` AS `ScreeningStrategySubWedge_multiplicity`, `v_datacollection_summary_screening`.`ScreeningStrategySubWedge_completeness` AS `ScreeningStrategySubWedge_completeness`, `v_datacollection_summary_screening`.`ScreeningStrategySubWedge_axisStart` AS `ScreeningStrategySubWedge_axisStart`, `Shipping`.`shippingId` AS `Shipping_shippingId`, `Shipping`.`shippingName` AS `Shipping_shippingName`, `Shipping`.`shippingStatus` AS `Shipping_shippingStatus`, `DiffractionPlan`.`diffractionPlanId` AS `diffractionPlanId`, `DiffractionPlan`.`experimentKind` AS `experimentKind`, `DiffractionPlan`.`observedResolution` AS `observedResolution`, `DiffractionPlan`.`minimalResolution` AS `minimalResolution`, `DiffractionPlan`.`exposureTime` AS `exposureTime`, `DiffractionPlan`.`oscillationRange` AS `oscillationRange`, `DiffractionPlan`.`maximalResolution` AS `maximalResolution`, `DiffractionPlan`.`screeningResolution` AS `screeningResolution`, `DiffractionPlan`.`radiationSensitivity` AS `radiationSensitivity`, `DiffractionPlan`.`anomalousScatterer` AS `anomalousScatterer`, `DiffractionPlan`.`preferredBeamSizeX` AS `preferredBeamSizeX`, `DiffractionPlan`.`preferredBeamSizeY` AS `preferredBeamSizeY`, `DiffractionPlan`.`preferredBeamDiameter` AS `preferredBeamDiameter`, `DiffractionPlan`.`comments` AS `DiffractipnPlan_comments`, `DiffractionPlan`.`aimedCompleteness` AS `aimedCompleteness`, `DiffractionPlan`.`aimedIOverSigmaAtHighestRes` AS `aimedIOverSigmaAtHighestRes`, `DiffractionPlan`.`aimedMultiplicity` AS `aimedMultiplicity`, `DiffractionPlan`.`aimedResolution` AS `aimedResolution`, `DiffractionPlan`.`anomalousData` AS `anomalousData`, `DiffractionPlan`.`complexity` AS `complexity`, `DiffractionPlan`.`estimateRadiationDamage` AS `estimateRadiationDamage`, `DiffractionPlan`.`forcedSpaceGroup` AS `forcedSpaceGroup`, `DiffractionPlan`.`requiredCompleteness` AS `requiredCompleteness`, `DiffractionPlan`.`requiredMultiplicity` AS `requiredMultiplicity`, `DiffractionPlan`.`requiredResolution` AS `requiredResolution`, `DiffractionPlan`.`strategyOption` AS `strategyOption`, `DiffractionPlan`.`kappaStrategyOption` AS `kappaStrategyOption`, `DiffractionPlan`.`numberOfPositions` AS `numberOfPositions`, `DiffractionPlan`.`minDimAccrossSpindleAxis` AS `minDimAccrossSpindleAxis`, `DiffractionPlan`.`maxDimAccrossSpindleAxis` AS `maxDimAccrossSpindleAxis`, `DiffractionPlan`.`radiationSensitivityBeta` AS `radiationSensitivityBeta`, `DiffractionPlan`.`radiationSensitivityGamma` AS `radiationSensitivityGamma`, `DiffractionPlan`.`minOscWidth` AS `minOscWidth`, `Detector`.`detectorType` AS `Detector_detectorType`, `Detector`.`detectorManufacturer` AS `Detector_detectorManufacturer`, `Detector`.`detectorModel` AS `Detector_detectorModel`, `Detector`.`detectorPixelSizeHorizontal` AS `Detector_detectorPixelSizeHorizontal`, `Detector`.`detectorPixelSizeVertical` AS `Detector_detectorPixelSizeVertical`, `Detector`.`detectorSerialNumber` AS `Detector_detectorSerialNumber`, `Detector`.`detectorDistanceMin` AS `Detector_detectorDistanceMin`, `Detector`.`detectorDistanceMax` AS `Detector_detectorDistanceMax`, `Detector`.`trustedPixelValueRangeLower` AS `Detector_trustedPixelValueRangeLower`, `Detector`.`trustedPixelValueRangeUpper` AS `Detector_trustedPixelValueRangeUpper`, `Detector`.`sensorThickness` AS `Detector_sensorThickness`, `Detector`.`overload` AS `Detector_overload`, `Detector`.`XGeoCorr` AS `Detector_XGeoCorr`, `Detector`.`YGeoCorr` AS `Detector_YGeoCorr`, `Detector`.`detectorMode` AS `Detector_detectorMode`, `BeamLineSetup`.`undulatorType1` AS `BeamLineSetup_undulatorType1`, `BeamLineSetup`.`undulatorType2` AS `BeamLineSetup_undulatorType2`, `BeamLineSetup`.`undulatorType3` AS `BeamLineSetup_undulatorType3`, `BeamLineSetup`.`synchrotronName` AS `BeamLineSetup_synchrotronName`, `BeamLineSetup`.`synchrotronMode` AS `BeamLineSetup_synchrotronMode`, `BeamLineSetup`.`polarisation` AS `BeamLineSetup_polarisation`, `BeamLineSetup`.`focusingOptic` AS `BeamLineSetup_focusingOptic`, `BeamLineSetup`.`beamDivergenceHorizontal` AS `BeamLineSetup_beamDivergenceHorizontal`, `BeamLineSetup`.`beamDivergenceVertical` AS `BeamLineSetup_beamDivergenceVertical`, `BeamLineSetup`.`monochromatorType` AS `BeamLineSetup_monochromatorType` FROM ((((((((((((((`DataCollectionGroup` left join `DataCollection` on(`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId` and `DataCollection`.`dataCollectionId` = (select max(`dc2`.`dataCollectionId`) from `DataCollection` `dc2` where `dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) left join `BLSession` on(`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)) left join `BeamLineSetup` on(`BeamLineSetup`.`beamLineSetupId` = `BLSession`.`beamLineSetupId`)) left join `Detector` on(`Detector`.`detectorId` = `DataCollection`.`detectorId`)) left join `BLSample` on(`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`)) left join `DiffractionPlan` on(`DiffractionPlan`.`diffractionPlanId` = `BLSample`.`diffractionPlanId`)) left join `Container` on(`Container`.`containerId` = `BLSample`.`containerId`)) left join `Dewar` on(`Container`.`dewarId` = `Dewar`.`dewarId`)) left join `Shipping` on(`Shipping`.`shippingId` = `Dewar`.`shippingId`)) left join `Crystal` on(`Crystal`.`crystalId` = `BLSample`.`crystalId`)) left join `Protein` on(`Protein`.`proteinId` = `Crystal`.`proteinId`)) left join `Workflow` on(`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)) left join `v_datacollection_summary_autoprocintegration` on(`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`)) left join `v_datacollection_summary_screening` on(`v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_autoprocintegration`
--
DROP TABLE IF EXISTS `v_datacollection_summary_autoprocintegration`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_autoprocintegration`  AS SELECT `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`, `AutoProcIntegration`.`cell_a` AS `cell_a`, `AutoProcIntegration`.`cell_b` AS `cell_b`, `AutoProcIntegration`.`cell_c` AS `cell_c`, `AutoProcIntegration`.`cell_alpha` AS `cell_alpha`, `AutoProcIntegration`.`cell_beta` AS `cell_beta`, `AutoProcIntegration`.`cell_gamma` AS `cell_gamma`, `AutoProcIntegration`.`anomalous` AS `anomalous`, `AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`, `AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_autoprocintegration_processingPrograms`, `AutoProcProgram`.`autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`, `AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_autoprocintegration_processingStatus`, `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_phasing_dataCollectionId`, `PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`, `SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`, `AutoProc`.`autoProcId` AS `autoProcId`, `AutoProc`.`spaceGroup` AS `AutoProc_spaceGroup`, `AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`, `AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`, `AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`, `AutoProcScalingStatistics`.`rMerge` AS `rMerge`, `AutoProcScalingStatistics`.`meanIOverSigI` AS `meanIOverSigI`, `AutoProcScalingStatistics`.`ccHalf` AS `ccHalf`, `AutoProcScalingStatistics`.`completeness` AS `completeness`, `AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId` FROM ((((((((`AutoProcIntegration` left join `AutoProcProgram` on(`AutoProcIntegration`.`autoProcProgramId` = `AutoProcProgram`.`autoProcProgramId`)) left join `AutoProcScaling_has_Int` on(`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`)) left join `AutoProcScaling` on(`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`)) left join `AutoProcScalingStatistics` on(`AutoProcScaling`.`autoProcScalingId` = `AutoProcScalingStatistics`.`autoProcScalingId`)) left join `AutoProc` on(`AutoProc`.`autoProcId` = `AutoProcScaling`.`autoProcId`)) left join `Phasing_has_Scaling` on(`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)) left join `PhasingStep` on(`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`)) left join `SpaceGroup` on(`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_datacollectiongroup`
--
DROP TABLE IF EXISTS `v_datacollection_summary_datacollectiongroup`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_datacollectiongroup`  AS SELECT `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`, `DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`, `DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`, `DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`, `DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`, `DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`, `DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`, `DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`, `DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`, `DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`, `BLSample`.`blSampleId` AS `BLSample_blSampleId`, `BLSample`.`crystalId` AS `BLSample_crystalId`, `BLSample`.`name` AS `BLSample_name`, `BLSample`.`code` AS `BLSample_code`, `BLSession`.`sessionId` AS `BLSession_sessionId`, `BLSession`.`proposalId` AS `BLSession_proposalId`, `BLSession`.`protectedData` AS `BLSession_protectedData`, `Protein`.`proteinId` AS `Protein_proteinId`, `Protein`.`name` AS `Protein_name`, `Protein`.`acronym` AS `Protein_acronym`, `DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`, `DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`, `DataCollection`.`startTime` AS `DataCollection_startTime`, `DataCollection`.`endTime` AS `DataCollection_endTime`, `DataCollection`.`runStatus` AS `DataCollection_runStatus`, `DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`, `DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`, `DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`, `DataCollection`.`exposureTime` AS `DataCollection_exposureTime`, `DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`, `DataCollection`.`wavelength` AS `DataCollection_wavelength`, `DataCollection`.`resolution` AS `DataCollection_resolution`, `DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`, `DataCollection`.`xBeam` AS `DataCollection_xBeam`, `DataCollection`.`yBeam` AS `DataCollection_yBeam`, `DataCollection`.`comments` AS `DataCollection_comments`, `DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`, `DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`, `DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`, `DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`, `DataCollection`.`phiStart` AS `DataCollection_phiStart`, `DataCollection`.`kappaStart` AS `DataCollection_kappaStart`, `DataCollection`.`omegaStart` AS `DataCollection_omegaStart`, `DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`, `DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`, `Workflow`.`workflowTitle` AS `Workflow_workflowTitle`, `Workflow`.`workflowType` AS `Workflow_workflowType`, `Workflow`.`status` AS `Workflow_status` FROM ((((((`DataCollectionGroup` left join `DataCollection` on(`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId` and `DataCollection`.`dataCollectionId` = (select max(`dc2`.`dataCollectionId`) from `DataCollection` `dc2` where `dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) left join `BLSession` on(`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)) left join `BLSample` on(`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`)) left join `Crystal` on(`Crystal`.`crystalId` = `BLSample`.`crystalId`)) left join `Protein` on(`Protein`.`proteinId` = `Crystal`.`proteinId`)) left join `Workflow` on(`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_phasing`
--
DROP TABLE IF EXISTS `v_datacollection_summary_phasing`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_phasing`  AS SELECT `PhasingStep`.`phasingStepId` AS `v_datacollection_summary_phasing_phasingStepId`, `PhasingStep`.`previousPhasingStepId` AS `v_datacollection_summary_phasing_previousPhasingStepId`, `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`, `AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`, `PhasingStep`.`phasingStepType` AS `v_datacollection_summary_phasing_phasingStepType`, `PhasingStep`.`method` AS `v_datacollection_summary_phasing_method`, `PhasingStep`.`solventContent` AS `v_datacollection_summary_phasing_solventContent`, `PhasingStep`.`enantiomorph` AS `v_datacollection_summary_phasing_enantiomorph`, `PhasingStep`.`lowRes` AS `v_datacollection_summary_phasing_lowRes`, `PhasingStep`.`highRes` AS `v_datacollection_summary_phasing_highRes`, `AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`, `SpaceGroup`.`spaceGroupShortName` AS `v_datacollection_summary_phasing_spaceGroupShortName` FROM (((((`AutoProcIntegration` left join `AutoProcScaling_has_Int` on(`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`)) left join `AutoProcScaling` on(`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`)) left join `Phasing_has_Scaling` on(`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)) left join `PhasingStep` on(`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`)) left join `SpaceGroup` on(`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_screening`
--
DROP TABLE IF EXISTS `v_datacollection_summary_screening`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_screening`  AS SELECT `Screening`.`screeningId` AS `Screening_screeningId`, `Screening`.`dataCollectionId` AS `Screening_dataCollectionId`, `Screening`.`dataCollectionGroupId` AS `Screening_dataCollectionGroupId`, `ScreeningOutput`.`strategySuccess` AS `ScreeningOutput_strategySuccess`, `ScreeningOutput`.`indexingSuccess` AS `ScreeningOutput_indexingSuccess`, `ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`, `ScreeningOutput`.`mosaicityEstimated` AS `ScreeningOutput_mosaicityEstimated`, `ScreeningOutput`.`mosaicity` AS `ScreeningOutput_mosaicity`, `ScreeningOutput`.`totalExposureTime` AS `ScreeningOutput_totalExposureTime`, `ScreeningOutput`.`totalRotationRange` AS `ScreeningOutput_totalRotationRange`, `ScreeningOutput`.`totalNumberOfImages` AS `ScreeningOutput_totalNumberOfImages`, `ScreeningOutputLattice`.`spaceGroup` AS `ScreeningOutputLattice_spaceGroup`, `ScreeningOutputLattice`.`unitCell_a` AS `ScreeningOutputLattice_unitCell_a`, `ScreeningOutputLattice`.`unitCell_b` AS `ScreeningOutputLattice_unitCell_b`, `ScreeningOutputLattice`.`unitCell_c` AS `ScreeningOutputLattice_unitCell_c`, `ScreeningOutputLattice`.`unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`, `ScreeningOutputLattice`.`unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`, `ScreeningOutputLattice`.`unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`, `ScreeningStrategySubWedge`.`exposureTime` AS `ScreeningStrategySubWedge_exposureTime`, `ScreeningStrategySubWedge`.`transmission` AS `ScreeningStrategySubWedge_transmission`, `ScreeningStrategySubWedge`.`oscillationRange` AS `ScreeningStrategySubWedge_oscillationRange`, `ScreeningStrategySubWedge`.`numberOfImages` AS `ScreeningStrategySubWedge_numberOfImages`, `ScreeningStrategySubWedge`.`multiplicity` AS `ScreeningStrategySubWedge_multiplicity`, `ScreeningStrategySubWedge`.`completeness` AS `ScreeningStrategySubWedge_completeness`, `ScreeningStrategySubWedge`.`axisStart` AS `ScreeningStrategySubWedge_axisStart`, `ScreeningStrategySubWedge`.`axisEnd` AS `ScreeningStrategySubWedge_axisEnd`, `ScreeningStrategySubWedge`.`rotationAxis` AS `ScreeningStrategySubWedge_rotationAxis` FROM (((((`Screening` left join `ScreeningOutput` on(`Screening`.`screeningId` = `ScreeningOutput`.`screeningId`)) left join `ScreeningOutputLattice` on(`ScreeningOutput`.`screeningOutputId` = `ScreeningOutputLattice`.`screeningOutputId`)) left join `ScreeningStrategy` on(`ScreeningStrategy`.`screeningOutputId` = `ScreeningOutput`.`screeningOutputId`)) left join `ScreeningStrategyWedge` on(`ScreeningStrategyWedge`.`screeningStrategyId` = `ScreeningStrategy`.`screeningStrategyId`)) left join `ScreeningStrategySubWedge` on(`ScreeningStrategySubWedge`.`screeningStrategyWedgeId` = `ScreeningStrategyWedge`.`screeningStrategyWedgeId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_dewar_summary`
--
DROP TABLE IF EXISTS `v_dewar_summary`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewar_summary`  AS SELECT `Shipping`.`shippingName` AS `shippingName`, `Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`, `Shipping`.`deliveryAgent_shippingDate` AS `deliveryAgent_shippingDate`, `Shipping`.`deliveryAgent_deliveryDate` AS `deliveryAgent_deliveryDate`, `Shipping`.`deliveryAgent_agentCode` AS `deliveryAgent_agentCode`, `Shipping`.`deliveryAgent_flightCode` AS `deliveryAgent_flightCode`, `Shipping`.`shippingStatus` AS `shippingStatus`, `Shipping`.`bltimeStamp` AS `bltimeStamp`, `Shipping`.`laboratoryId` AS `laboratoryId`, `Shipping`.`isStorageShipping` AS `isStorageShipping`, `Shipping`.`creationDate` AS `creationDate`, `Shipping`.`comments` AS `Shipping_comments`, `Shipping`.`sendingLabContactId` AS `sendingLabContactId`, `Shipping`.`returnLabContactId` AS `returnLabContactId`, `Shipping`.`returnCourier` AS `returnCourier`, `Shipping`.`dateOfShippingToUser` AS `dateOfShippingToUser`, `Shipping`.`shippingType` AS `shippingType`, `Dewar`.`dewarId` AS `dewarId`, `Dewar`.`shippingId` AS `shippingId`, `Dewar`.`code` AS `dewarCode`, `Dewar`.`comments` AS `comments`, `Dewar`.`storageLocation` AS `storageLocation`, `Dewar`.`dewarStatus` AS `dewarStatus`, `Dewar`.`isStorageDewar` AS `isStorageDewar`, `Dewar`.`barCode` AS `barCode`, `Dewar`.`firstExperimentId` AS `firstExperimentId`, `Dewar`.`customsValue` AS `customsValue`, `Dewar`.`transportValue` AS `transportValue`, `Dewar`.`trackingNumberToSynchrotron` AS `trackingNumberToSynchrotron`, `Dewar`.`trackingNumberFromSynchrotron` AS `trackingNumberFromSynchrotron`, `Dewar`.`type` AS `type`, `Dewar`.`isReimbursed` AS `isReimbursed`, `BLSession`.`sessionId` AS `sessionId`, `BLSession`.`beamLineName` AS `beamlineName`, `BLSession`.`startDate` AS `sessionStartDate`, `BLSession`.`endDate` AS `sessionEndDate`, `BLSession`.`beamLineOperator` AS `beamLineOperator`, `BLSession`.`nbReimbDewars` AS `nbReimbDewars`, `Proposal`.`proposalId` AS `proposalId`, `Container`.`containerId` AS `containerId`, `Container`.`containerType` AS `containerType`, `Container`.`capacity` AS `capacity`, `Container`.`beamlineLocation` AS `beamlineLocation`, `Container`.`sampleChangerLocation` AS `sampleChangerLocation`, `Container`.`containerStatus` AS `containerStatus`, `Container`.`code` AS `containerCode` FROM (((((`Dewar` left join `Container` on(`Container`.`dewarId` = `Dewar`.`dewarId`)) left join `Shipping` on(`Shipping`.`shippingId` = `Dewar`.`shippingId`)) left join `ShippingHasSession` on(`Shipping`.`shippingId` = `ShippingHasSession`.`shippingId`)) left join `BLSession` on(`ShippingHasSession`.`sessionId` = `BLSession`.`sessionId`)) left join `Proposal` on(`Proposal`.`proposalId` = `Shipping`.`proposalId`)) ORDER BY `Dewar`.`dewarId` DESC ;

-- --------------------------------------------------------

--
-- Structure for view `v_em_classification`
--
DROP TABLE IF EXISTS `v_em_classification`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_em_classification`  AS SELECT `Proposal`.`proposalId` AS `proposalId`, `BLSession`.`sessionId` AS `sessionId`, `DataCollection`.`imageDirectory` AS `imageDirectory`, `ParticlePicker`.`particlePickerId` AS `particlePickerId`, `ParticlePicker`.`numberOfParticles` AS `numberOfParticles`, `ParticleClassificationGroup`.`particleClassificationGroupId` AS `particleClassificationGroupId`, `ParticleClassification`.`particleClassificationId` AS `particleClassificationId`, `ParticleClassification`.`classNumber` AS `classNumber`, `ParticleClassification`.`classImageFullPath` AS `classImageFullPath`, `ParticleClassification`.`particlesPerClass` AS `particlesPerClass`, `ParticleClassification`.`classDistribution` AS `classDistribution`, `ParticleClassification`.`rotationAccuracy` AS `rotationAccuracy`, `ParticleClassification`.`translationAccuracy` AS `translationAccuracy`, `ParticleClassification`.`estimatedResolution` AS `estimatedResolution`, `ParticleClassification`.`overallFourierCompleteness` AS `overallFourierCompleteness` FROM ((((((((`BLSession` join `Proposal` on(`Proposal`.`proposalId` = `BLSession`.`proposalId`)) join `DataCollectionGroup` on(`DataCollectionGroup`.`sessionId` = `BLSession`.`sessionId`)) join `DataCollection` on(`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)) join `Movie` on(`Movie`.`dataCollectionId` = `DataCollection`.`dataCollectionId`)) join `MotionCorrection` on(`MotionCorrection`.`movieId` = `Movie`.`movieId`)) join `ParticlePicker` on(`ParticlePicker`.`firstMotionCorrectionId` = `MotionCorrection`.`motionCorrectionId`)) join `ParticleClassificationGroup` on(`ParticleClassificationGroup`.`particlePickerId` = `ParticlePicker`.`particlePickerId`)) join `ParticleClassification` on(`ParticleClassification`.`particleClassificationGroupId` = `ParticleClassificationGroup`.`particleClassificationGroupId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_energyScan`
--
DROP TABLE IF EXISTS `v_energyScan`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_energyScan`  AS SELECT `EnergyScan`.`energyScanId` AS `energyScanId`, `EnergyScan`.`sessionId` AS `sessionId`, `EnergyScan`.`blSampleId` AS `blSampleId`, `EnergyScan`.`fluorescenceDetector` AS `fluorescenceDetector`, `EnergyScan`.`scanFileFullPath` AS `scanFileFullPath`, `EnergyScan`.`choochFileFullPath` AS `choochFileFullPath`, `EnergyScan`.`jpegChoochFileFullPath` AS `jpegChoochFileFullPath`, `EnergyScan`.`element` AS `element`, `EnergyScan`.`startEnergy` AS `startEnergy`, `EnergyScan`.`endEnergy` AS `endEnergy`, `EnergyScan`.`transmissionFactor` AS `transmissionFactor`, `EnergyScan`.`exposureTime` AS `exposureTime`, `EnergyScan`.`synchrotronCurrent` AS `synchrotronCurrent`, `EnergyScan`.`temperature` AS `temperature`, `EnergyScan`.`peakEnergy` AS `peakEnergy`, `EnergyScan`.`peakFPrime` AS `peakFPrime`, `EnergyScan`.`peakFDoublePrime` AS `peakFDoublePrime`, `EnergyScan`.`inflectionEnergy` AS `inflectionEnergy`, `EnergyScan`.`inflectionFPrime` AS `inflectionFPrime`, `EnergyScan`.`inflectionFDoublePrime` AS `inflectionFDoublePrime`, `EnergyScan`.`xrayDose` AS `xrayDose`, `EnergyScan`.`startTime` AS `startTime`, `EnergyScan`.`endTime` AS `endTime`, `EnergyScan`.`edgeEnergy` AS `edgeEnergy`, `EnergyScan`.`filename` AS `filename`, `EnergyScan`.`beamSizeVertical` AS `beamSizeVertical`, `EnergyScan`.`beamSizeHorizontal` AS `beamSizeHorizontal`, `EnergyScan`.`crystalClass` AS `crystalClass`, `EnergyScan`.`comments` AS `comments`, `EnergyScan`.`flux` AS `flux`, `EnergyScan`.`flux_end` AS `flux_end`, `BLSample`.`blSampleId` AS `BLSample_sampleId`, `BLSample`.`name` AS `name`, `BLSample`.`code` AS `code`, `Protein`.`acronym` AS `acronym`, `BLSession`.`proposalId` AS `BLSession_proposalId` FROM ((((`EnergyScan` left join `BLSample` on(`BLSample`.`blSampleId` = `EnergyScan`.`blSampleId`)) left join `Crystal` on(`Crystal`.`crystalId` = `BLSample`.`crystalId`)) left join `Protein` on(`Protein`.`proteinId` = `Crystal`.`proteinId`)) left join `BLSession` on(`BLSession`.`sessionId` = `EnergyScan`.`sessionId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_mx_autoprocessing_stats`
--
DROP TABLE IF EXISTS `v_mx_autoprocessing_stats`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_mx_autoprocessing_stats`  AS SELECT `AutoProcScalingStatistics`.`autoProcScalingStatisticsId` AS `autoProcScalingStatisticsId`, `AutoProcScalingStatistics`.`autoProcScalingId` AS `autoProcScalingId`, `AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`, `AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`, `AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`, `AutoProcScalingStatistics`.`rMerge` AS `rMerge`, `AutoProcScalingStatistics`.`rMeasWithinIPlusIMinus` AS `rMeasWithinIPlusIMinus`, `AutoProcScalingStatistics`.`rMeasAllIPlusIMinus` AS `rMeasAllIPlusIMinus`, `AutoProcScalingStatistics`.`rPimWithinIPlusIMinus` AS `rPimWithinIPlusIMinus`, `AutoProcScalingStatistics`.`rPimAllIPlusIMinus` AS `rPimAllIPlusIMinus`, `AutoProcScalingStatistics`.`fractionalPartialBias` AS `fractionalPartialBias`, `AutoProcScalingStatistics`.`nTotalObservations` AS `nTotalObservations`, `AutoProcScalingStatistics`.`nTotalUniqueObservations` AS `nTotalUniqueObservations`, `AutoProcScalingStatistics`.`meanIOverSigI` AS `meanIOverSigI`, `AutoProcScalingStatistics`.`completeness` AS `completeness`, `AutoProcScalingStatistics`.`multiplicity` AS `multiplicity`, `AutoProcScalingStatistics`.`anomalousCompleteness` AS `anomalousCompleteness`, `AutoProcScalingStatistics`.`anomalousMultiplicity` AS `anomalousMultiplicity`, `AutoProcScalingStatistics`.`recordTimeStamp` AS `recordTimeStamp`, `AutoProcScalingStatistics`.`anomalous` AS `anomalous`, `AutoProcScalingStatistics`.`ccHalf` AS `ccHalf`, `AutoProcScalingStatistics`.`ccAno` AS `ccAno`, `AutoProcScalingStatistics`.`sigAno` AS `sigAno`, `AutoProcScalingStatistics`.`isa` AS `ISA`, `DataCollection`.`dataCollectionId` AS `dataCollectionId`, `DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`, `DataCollection`.`detectorId` AS `detectorId`, `DataCollection`.`blSubSampleId` AS `blSubSampleId`, `DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`, `DataCollection`.`startTime` AS `startTime`, `DataCollection`.`endTime` AS `endTime`, `BLSession`.`sessionId` AS `sessionId`, `BLSession`.`proposalId` AS `proposalId`, `BLSession`.`beamLineName` AS `beamLineName` FROM ((((((`AutoProcScalingStatistics` left join `AutoProcScaling` on(`AutoProcScalingStatistics`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)) left join `AutoProcScaling_has_Int` on(`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)) left join `AutoProcIntegration` on(`AutoProcIntegration`.`autoProcIntegrationId` = `AutoProcScaling_has_Int`.`autoProcIntegrationId`)) left join `DataCollection` on(`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`)) left join `DataCollectionGroup` on(`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)) left join `BLSession` on(`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_mx_experiment_stats`
--
DROP TABLE IF EXISTS `v_mx_experiment_stats`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_mx_experiment_stats`  AS SELECT `DC`.`startTime` AS `startTime`, `DC`.`numberOfImages` AS `Images`, `DC`.`transmission` AS `Transmission`, `DC`.`resolution` AS `Res. (corner)`, `DC`.`wavelength` AS `En. (Wave.)`, `DC`.`omegaStart` AS `Omega start (total)`, `DC`.`exposureTime` AS `Exposure Time`, `DC`.`flux` AS `Flux`, `DC`.`flux_end` AS `Flux End`, `DC`.`detectorDistance` AS `Detector Distance`, `DC`.`xBeam` AS `X Beam`, `DC`.`yBeam` AS `Y Beam`, `DC`.`kappaStart` AS `Kappa`, `DC`.`phiStart` AS `Phi`, `DC`.`axisStart` AS `Axis Start`, `DC`.`axisEnd` AS `Axis End`, `DC`.`axisRange` AS `Axis Range`, `DC`.`beamSizeAtSampleX` AS `Beam Size X`, `DC`.`beamSizeAtSampleY` AS `Beam Size Y`, `BLS`.`beamLineName` AS `beamLineName`, `DCG`.`comments` AS `comments`, `P`.`proposalNumber` AS `proposalNumber` FROM (((`DataCollection` `DC` join `DataCollectionGroup` `DCG` on(`DCG`.`dataCollectionGroupId` = `DC`.`dataCollectionGroupId`)) join `BLSession` `BLS` on(`BLS`.`sessionId` = `DCG`.`sessionId`)) join `Proposal` `P` on(`P`.`proposalId` = `BLS`.`proposalId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_mx_sample`
--
DROP TABLE IF EXISTS `v_mx_sample`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_mx_sample`  AS SELECT `BLSample`.`blSampleId` AS `BLSample_blSampleId`, `BLSample`.`diffractionPlanId` AS `BLSample_diffractionPlanId`, `BLSample`.`crystalId` AS `BLSample_crystalId`, `BLSample`.`containerId` AS `BLSample_containerId`, `BLSample`.`name` AS `BLSample_name`, `BLSample`.`code` AS `BLSample_code`, `BLSample`.`location` AS `BLSample_location`, `BLSample`.`holderLength` AS `BLSample_holderLength`, `BLSample`.`loopLength` AS `BLSample_loopLength`, `BLSample`.`loopType` AS `BLSample_loopType`, `BLSample`.`wireWidth` AS `BLSample_wireWidth`, `BLSample`.`comments` AS `BLSample_comments`, `BLSample`.`completionStage` AS `BLSample_completionStage`, `BLSample`.`structureStage` AS `BLSample_structureStage`, `BLSample`.`publicationStage` AS `BLSample_publicationStage`, `BLSample`.`publicationComments` AS `BLSample_publicationComments`, `BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`, `BLSample`.`isInSampleChanger` AS `BLSample_isInSampleChanger`, `BLSample`.`lastKnownCenteringPosition` AS `BLSample_lastKnownCenteringPosition`, `BLSample`.`recordTimeStamp` AS `BLSample_recordTimeStamp`, `BLSample`.`SMILES` AS `BLSample_SMILES`, `Protein`.`proteinId` AS `Protein_proteinId`, `Protein`.`name` AS `Protein_name`, `Protein`.`acronym` AS `Protein_acronym`, `Protein`.`proteinType` AS `Protein_proteinType`, `Shipping`.`proposalId` AS `Protein_proposalId`, `Person`.`personId` AS `Person_personId`, `Person`.`familyName` AS `Person_familyName`, `Person`.`givenName` AS `Person_givenName`, `Person`.`emailAddress` AS `Person_emailAddress`, `Container`.`containerId` AS `Container_containerId`, `Container`.`code` AS `Container_code`, `Container`.`containerType` AS `Container_containerType`, `Container`.`containerStatus` AS `Container_containerStatus`, `Container`.`beamlineLocation` AS `Container_beamlineLocation`, `Container`.`sampleChangerLocation` AS `Container_sampleChangerLocation`, `Dewar`.`code` AS `Dewar_code`, `Dewar`.`dewarId` AS `Dewar_dewarId`, `Dewar`.`storageLocation` AS `Dewar_storageLocation`, `Dewar`.`dewarStatus` AS `Dewar_dewarStatus`, `Dewar`.`barCode` AS `Dewar_barCode`, `Shipping`.`shippingId` AS `Shipping_shippingId`, `BLSession`.`sessionId` AS `sessionId`, `BLSession`.`startDate` AS `BLSession_startDate`, `BLSession`.`beamLineName` AS `BLSession_beamLineName` FROM ((((((((`BLSample` left join `Crystal` on(`Crystal`.`crystalId` = `BLSample`.`crystalId`)) left join `Protein` on(`Protein`.`proteinId` = `Crystal`.`proteinId`)) left join `Person` on(`Person`.`personId` = `Protein`.`personId`)) left join `Container` on(`BLSample`.`containerId` = `Container`.`containerId`)) left join `Dewar` on(`Dewar`.`dewarId` = `Container`.`dewarId`)) left join `Shipping` on(`Dewar`.`shippingId` = `Shipping`.`shippingId`)) left join `DataCollectionGroup` on(`DataCollectionGroup`.`blSampleId` = `BLSample`.`blSampleId`)) left join `BLSession` on(`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_phasing`
--
DROP TABLE IF EXISTS `v_phasing`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_phasing`  AS SELECT `BLSample`.`blSampleId` AS `BLSample_blSampleId`, `AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`, `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`, `AutoProcIntegration`.`autoProcProgramId` AS `AutoProcIntegration_autoProcProgramId`, `AutoProcIntegration`.`startImageNumber` AS `AutoProcIntegration_startImageNumber`, `AutoProcIntegration`.`endImageNumber` AS `AutoProcIntegration_endImageNumber`, `AutoProcIntegration`.`refinedDetectorDistance` AS `AutoProcIntegration_refinedDetectorDistance`, `AutoProcIntegration`.`refinedXBeam` AS `AutoProcIntegration_refinedXBeam`, `AutoProcIntegration`.`refinedYBeam` AS `AutoProcIntegration_refinedYBeam`, `AutoProcIntegration`.`rotationAxisX` AS `AutoProcIntegration_rotationAxisX`, `AutoProcIntegration`.`rotationAxisY` AS `AutoProcIntegration_rotationAxisY`, `AutoProcIntegration`.`rotationAxisZ` AS `AutoProcIntegration_rotationAxisZ`, `AutoProcIntegration`.`beamVectorX` AS `AutoProcIntegration_beamVectorX`, `AutoProcIntegration`.`beamVectorY` AS `AutoProcIntegration_beamVectorY`, `AutoProcIntegration`.`beamVectorZ` AS `AutoProcIntegration_beamVectorZ`, `AutoProcIntegration`.`cell_a` AS `AutoProcIntegration_cell_a`, `AutoProcIntegration`.`cell_b` AS `AutoProcIntegration_cell_b`, `AutoProcIntegration`.`cell_c` AS `AutoProcIntegration_cell_c`, `AutoProcIntegration`.`cell_alpha` AS `AutoProcIntegration_cell_alpha`, `AutoProcIntegration`.`cell_beta` AS `AutoProcIntegration_cell_beta`, `AutoProcIntegration`.`cell_gamma` AS `AutoProcIntegration_cell_gamma`, `AutoProcIntegration`.`recordTimeStamp` AS `AutoProcIntegration_recordTimeStamp`, `AutoProcIntegration`.`anomalous` AS `AutoProcIntegration_anomalous`, `SpaceGroup`.`spaceGroupId` AS `SpaceGroup_spaceGroupId`, `SpaceGroup`.`geometryClassnameId` AS `SpaceGroup_geometryClassnameId`, `SpaceGroup`.`spaceGroupNumber` AS `SpaceGroup_spaceGroupNumber`, `SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`, `SpaceGroup`.`spaceGroupName` AS `SpaceGroup_spaceGroupName`, `SpaceGroup`.`bravaisLattice` AS `SpaceGroup_bravaisLattice`, `SpaceGroup`.`bravaisLatticeName` AS `SpaceGroup_bravaisLatticeName`, `SpaceGroup`.`pointGroup` AS `SpaceGroup_pointGroup`, `SpaceGroup`.`MX_used` AS `SpaceGroup_MX_used`, `PhasingStep`.`phasingStepId` AS `PhasingStep_phasingStepId`, `PhasingStep`.`previousPhasingStepId` AS `PhasingStep_previousPhasingStepId`, `PhasingStep`.`programRunId` AS `PhasingStep_programRunId`, `PhasingStep`.`spaceGroupId` AS `PhasingStep_spaceGroupId`, `PhasingStep`.`autoProcScalingId` AS `PhasingStep_autoProcScalingId`, `PhasingStep`.`phasingAnalysisId` AS `PhasingStep_phasingAnalysisId`, `PhasingStep`.`phasingStepType` AS `PhasingStep_phasingStepType`, `PhasingStep`.`method` AS `PhasingStep_method`, `PhasingStep`.`solventContent` AS `PhasingStep_solventContent`, `PhasingStep`.`enantiomorph` AS `PhasingStep_enantiomorph`, `PhasingStep`.`lowRes` AS `PhasingStep_lowRes`, `PhasingStep`.`highRes` AS `PhasingStep_highRes`, `PhasingStep`.`recordTimeStamp` AS `PhasingStep_recordTimeStamp`, `DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`, `DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`, `DataCollection`.`strategySubWedgeOrigId` AS `DataCollection_strategySubWedgeOrigId`, `DataCollection`.`detectorId` AS `DataCollection_detectorId`, `DataCollection`.`blSubSampleId` AS `DataCollection_blSubSampleId`, `DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`, `DataCollection`.`startTime` AS `DataCollection_startTime`, `DataCollection`.`endTime` AS `DataCollection_endTime`, `DataCollection`.`runStatus` AS `DataCollection_runStatus`, `DataCollection`.`axisStart` AS `DataCollection_axisStart`, `DataCollection`.`axisEnd` AS `DataCollection_axisEnd`, `DataCollection`.`axisRange` AS `DataCollection_axisRange`, `DataCollection`.`overlap` AS `DataCollection_overlap`, `DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`, `DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`, `DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`, `DataCollection`.`exposureTime` AS `DataCollection_exposureTime`, `DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`, `DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`, `DataCollection`.`imageSuffix` AS `DataCollection_imageSuffix`, `DataCollection`.`fileTemplate` AS `DataCollection_fileTemplate`, `DataCollection`.`wavelength` AS `DataCollection_wavelength`, `DataCollection`.`resolution` AS `DataCollection_resolution`, `DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`, `DataCollection`.`xBeam` AS `DataCollection_xBeam`, `DataCollection`.`yBeam` AS `DataCollection_yBeam`, `DataCollection`.`xBeamPix` AS `DataCollection_xBeamPix`, `DataCollection`.`yBeamPix` AS `DataCollection_yBeamPix`, `DataCollection`.`comments` AS `DataCollection_comments`, `DataCollection`.`printableForReport` AS `DataCollection_printableForReport`, `DataCollection`.`slitGapVertical` AS `DataCollection_slitGapVertical`, `DataCollection`.`slitGapHorizontal` AS `DataCollection_slitGapHorizontal`, `DataCollection`.`transmission` AS `DataCollection_transmission`, `DataCollection`.`synchrotronMode` AS `DataCollection_synchrotronMode`, `DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`, `DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`, `DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`, `DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`, `DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`, `DataCollection`.`phiStart` AS `DataCollection_phiStart`, `DataCollection`.`kappaStart` AS `DataCollection_kappaStart`, `DataCollection`.`omegaStart` AS `DataCollection_omegaStart`, `DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`, `DataCollection`.`detector2Theta` AS `DataCollection_detector2Theta`, `DataCollection`.`undulatorGap1` AS `DataCollection_undulatorGap1`, `DataCollection`.`undulatorGap2` AS `DataCollection_undulatorGap2`, `DataCollection`.`undulatorGap3` AS `DataCollection_undulatorGap3`, `DataCollection`.`beamSizeAtSampleX` AS `DataCollection_beamSizeAtSampleX`, `DataCollection`.`beamSizeAtSampleY` AS `DataCollection_beamSizeAtSampleY`, `DataCollection`.`centeringMethod` AS `DataCollection_centeringMethod`, `DataCollection`.`averageTemperature` AS `DataCollection_averageTemperature`, `DataCollection`.`actualCenteringPosition` AS `DataCollection_actualCenteringPosition`, `DataCollection`.`beamShape` AS `DataCollection_beamShape`, `DataCollection`.`flux` AS `DataCollection_flux`, `DataCollection`.`flux_end` AS `DataCollection_flux_end`, `DataCollection`.`totalAbsorbedDose` AS `DataCollection_totalAbsorbedDose`, `DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`, `DataCollection`.`imageQualityIndicatorsPlotPath` AS `DataCollection_imageQualityIndicatorsPlotPath`, `DataCollection`.`imageQualityIndicatorsCSVPath` AS `DataCollection_imageQualityIndicatorsCSVPath`, `PhasingProgramRun`.`phasingProgramRunId` AS `PhasingProgramRun_phasingProgramRunId`, `PhasingProgramRun`.`phasingCommandLine` AS `PhasingProgramRun_phasingCommandLine`, `PhasingProgramRun`.`phasingPrograms` AS `PhasingProgramRun_phasingPrograms`, `PhasingProgramRun`.`phasingStatus` AS `PhasingProgramRun_phasingStatus`, `PhasingProgramRun`.`phasingMessage` AS `PhasingProgramRun_phasingMessage`, `PhasingProgramRun`.`phasingStartTime` AS `PhasingProgramRun_phasingStartTime`, `PhasingProgramRun`.`phasingEndTime` AS `PhasingProgramRun_phasingEndTime`, `PhasingProgramRun`.`phasingEnvironment` AS `PhasingProgramRun_phasingEnvironment`, `PhasingProgramRun`.`phasingDirectory` AS `PhasingProgramRun_phasingDirectory`, `PhasingProgramRun`.`recordTimeStamp` AS `PhasingProgramRun_recordTimeStamp`, `Protein`.`proteinId` AS `Protein_proteinId`, `BLSession`.`sessionId` AS `BLSession_sessionId`, `BLSession`.`proposalId` AS `BLSession_proposalId`, `PhasingStatistics`.`phasingStatisticsId` AS `PhasingStatistics_phasingStatisticsId`, `PhasingStatistics`.`metric` AS `PhasingStatistics_metric`, `PhasingStatistics`.`statisticsValue` AS `PhasingStatistics_statisticsValue` FROM (((((((((((((`DataCollection` left join `AutoProcIntegration` on(`AutoProcIntegration`.`dataCollectionId` = `DataCollection`.`dataCollectionId`)) left join `AutoProcScaling_has_Int` on(`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`)) left join `AutoProcScaling` on(`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`)) left join `PhasingStep` on(`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)) left join `PhasingStatistics` on(`PhasingStatistics`.`phasingStepId` = `PhasingStep`.`phasingStepId`)) left join `SpaceGroup` on(`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)) left join `DataCollectionGroup` on(`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)) left join `BLSession` on(`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)) left join `Proposal` on(`Proposal`.`proposalId` = `BLSession`.`proposalId`)) left join `BLSample` on(`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`)) left join `Crystal` on(`Crystal`.`crystalId` = `BLSample`.`crystalId`)) left join `Protein` on(`Crystal`.`proteinId` = `Protein`.`proteinId`)) left join `PhasingProgramRun` on(`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_session`
--
DROP TABLE IF EXISTS `v_session`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_session`  AS SELECT `BLSession`.`sessionId` AS `sessionId`, `BLSession`.`expSessionPk` AS `expSessionPk`, `BLSession`.`beamLineSetupId` AS `beamLineSetupId`, `BLSession`.`proposalId` AS `proposalId`, `BLSession`.`projectCode` AS `projectCode`, `BLSession`.`startDate` AS `BLSession_startDate`, `BLSession`.`endDate` AS `BLSession_endDate`, `BLSession`.`beamLineName` AS `beamLineName`, `BLSession`.`scheduled` AS `scheduled`, `BLSession`.`nbShifts` AS `nbShifts`, `BLSession`.`comments` AS `comments`, `BLSession`.`beamLineOperator` AS `beamLineOperator`, `BLSession`.`visit_number` AS `visit_number`, `BLSession`.`bltimeStamp` AS `bltimeStamp`, `BLSession`.`usedFlag` AS `usedFlag`, `BLSession`.`sessionTitle` AS `sessionTitle`, `BLSession`.`structureDeterminations` AS `structureDeterminations`, `BLSession`.`dewarTransport` AS `dewarTransport`, `BLSession`.`databackupFrance` AS `databackupFrance`, `BLSession`.`databackupEurope` AS `databackupEurope`, `BLSession`.`operatorSiteNumber` AS `operatorSiteNumber`, `BLSession`.`lastUpdate` AS `BLSession_lastUpdate`, `BLSession`.`protectedData` AS `BLSession_protectedData`, `Proposal`.`title` AS `Proposal_title`, `Proposal`.`proposalCode` AS `Proposal_proposalCode`, `Proposal`.`proposalNumber` AS `Proposal_ProposalNumber`, `Proposal`.`proposalType` AS `Proposal_ProposalType`, `Person`.`personId` AS `Person_personId`, `Person`.`familyName` AS `Person_familyName`, `Person`.`givenName` AS `Person_givenName`, `Person`.`emailAddress` AS `Person_emailAddress` FROM ((`BLSession` left join `Proposal` on(`Proposal`.`proposalId` = `BLSession`.`proposalId`)) left join `Person` on(`Person`.`personId` = `Proposal`.`personId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_tracking_shipment_history`
--
DROP TABLE IF EXISTS `v_tracking_shipment_history`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`localhost` SQL SECURITY DEFINER VIEW `v_tracking_shipment_history`  AS SELECT `Dewar`.`dewarId` AS `Dewar_dewarId`, `Dewar`.`code` AS `Dewar_code`, `Dewar`.`comments` AS `Dewar_comments`, `Dewar`.`dewarStatus` AS `Dewar_dewarStatus`, `Dewar`.`barCode` AS `Dewar_barCode`, `Dewar`.`firstExperimentId` AS `Dewar_firstExperimentId`, `Dewar`.`trackingNumberToSynchrotron` AS `Dewar_trackingNumberToSynchrotron`, `Dewar`.`trackingNumberFromSynchrotron` AS `Dewar_trackingNumberFromSynchrotron`, `Dewar`.`type` AS `Dewar_type`, `Shipping`.`shippingId` AS `Shipping_shippingId`, `Shipping`.`proposalId` AS `Shipping_proposalId`, `Shipping`.`shippingName` AS `Shipping_shippingName`, `Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`, `Shipping`.`deliveryAgent_shippingDate` AS `Shipping_deliveryAgent_shippingDate`, `Shipping`.`deliveryAgent_deliveryDate` AS `Shipping_deliveryAgent_deliveryDate`, `Shipping`.`shippingStatus` AS `Shipping_shippingStatus`, `Shipping`.`returnCourier` AS `Shipping_returnCourier`, `Shipping`.`dateOfShippingToUser` AS `Shipping_dateOfShippingToUser`, `DewarTransportHistory`.`DewarTransportHistoryId` AS `DewarTransportHistory_DewarTransportHistoryId`, `DewarTransportHistory`.`dewarStatus` AS `DewarTransportHistory_dewarStatus`, `DewarTransportHistory`.`storageLocation` AS `DewarTransportHistory_storageLocation`, `DewarTransportHistory`.`arrivalDate` AS `DewarTransportHistory_arrivalDate` FROM ((`Shipping` left join `Dewar` on(`Dewar`.`shippingId` = `Shipping`.`shippingId`)) left join `DewarTransportHistory` on(`DewarTransportHistory`.`dewarId` = `Dewar`.`dewarId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_xfeFluorescenceSpectrum`
--
DROP TABLE IF EXISTS `v_xfeFluorescenceSpectrum`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_xfeFluorescenceSpectrum`  AS SELECT `XFEFluorescenceSpectrum`.`xfeFluorescenceSpectrumId` AS `xfeFluorescenceSpectrumId`, `XFEFluorescenceSpectrum`.`sessionId` AS `sessionId`, `XFEFluorescenceSpectrum`.`blSampleId` AS `blSampleId`, `XFEFluorescenceSpectrum`.`fittedDataFileFullPath` AS `fittedDataFileFullPath`, `XFEFluorescenceSpectrum`.`scanFileFullPath` AS `scanFileFullPath`, `XFEFluorescenceSpectrum`.`jpegScanFileFullPath` AS `jpegScanFileFullPath`, `XFEFluorescenceSpectrum`.`startTime` AS `startTime`, `XFEFluorescenceSpectrum`.`endTime` AS `endTime`, `XFEFluorescenceSpectrum`.`filename` AS `filename`, `XFEFluorescenceSpectrum`.`energy` AS `energy`, `XFEFluorescenceSpectrum`.`exposureTime` AS `exposureTime`, `XFEFluorescenceSpectrum`.`beamTransmission` AS `beamTransmission`, `XFEFluorescenceSpectrum`.`annotatedPymcaXfeSpectrum` AS `annotatedPymcaXfeSpectrum`, `XFEFluorescenceSpectrum`.`beamSizeVertical` AS `beamSizeVertical`, `XFEFluorescenceSpectrum`.`beamSizeHorizontal` AS `beamSizeHorizontal`, `XFEFluorescenceSpectrum`.`crystalClass` AS `crystalClass`, `XFEFluorescenceSpectrum`.`comments` AS `comments`, `XFEFluorescenceSpectrum`.`flux` AS `flux`, `XFEFluorescenceSpectrum`.`flux_end` AS `flux_end`, `XFEFluorescenceSpectrum`.`workingDirectory` AS `workingDirectory`, `BLSample`.`blSampleId` AS `BLSample_sampleId`, `BLSession`.`proposalId` AS `BLSession_proposalId` FROM ((`XFEFluorescenceSpectrum` left join `BLSample` on(`BLSample`.`blSampleId` = `XFEFluorescenceSpectrum`.`blSampleId`)) left join `BLSession` on(`BLSession`.`sessionId` = `XFEFluorescenceSpectrum`.`sessionId`)) ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `AbInitioModel`
--
ALTER TABLE `AbInitioModel`
  ADD PRIMARY KEY (`abInitioModelId`),
  ADD KEY `AbInitioModelToModelList` (`modelListId`),
  ADD KEY `AverageToModel` (`averagedModelId`),
  ADD KEY `AbInitioModelToRapid` (`rapidShapeDeterminationModelId`),
  ADD KEY `SahpeDeterminationToAbiniti` (`shapeDeterminationModelId`);

--
-- Indexes for table `Additive`
--
ALTER TABLE `Additive`
  ADD PRIMARY KEY (`additiveId`);

--
-- Indexes for table `AdminActivity`
--
ALTER TABLE `AdminActivity`
  ADD PRIMARY KEY (`adminActivityId`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `AdminActivity_FKAction` (`action`);

--
-- Indexes for table `AdminVar`
--
ALTER TABLE `AdminVar`
  ADD PRIMARY KEY (`varId`),
  ADD KEY `AdminVar_FKIndexName` (`name`),
  ADD KEY `AdminVar_FKIndexValue` (`value`(767));

--
-- Indexes for table `Aperture`
--
ALTER TABLE `Aperture`
  ADD PRIMARY KEY (`apertureId`);

--
-- Indexes for table `Assembly`
--
ALTER TABLE `Assembly`
  ADD PRIMARY KEY (`assemblyId`),
  ADD KEY `AssemblyToMacromolecule` (`macromoleculeId`);

--
-- Indexes for table `AssemblyHasMacromolecule`
--
ALTER TABLE `AssemblyHasMacromolecule`
  ADD PRIMARY KEY (`AssemblyHasMacromoleculeId`),
  ADD KEY `AssemblyHasMacromoleculeToAssembly` (`assemblyId`),
  ADD KEY `AssemblyHasMacromoleculeToAssemblyRegion` (`macromoleculeId`);

--
-- Indexes for table `AssemblyRegion`
--
ALTER TABLE `AssemblyRegion`
  ADD PRIMARY KEY (`assemblyRegionId`),
  ADD KEY `AssemblyRegionToAssemblyHasMacromolecule` (`assemblyHasMacromoleculeId`);

--
-- Indexes for table `AutoProc`
--
ALTER TABLE `AutoProc`
  ADD PRIMARY KEY (`autoProcId`),
  ADD KEY `AutoProc_FKIndex1` (`autoProcProgramId`);

--
-- Indexes for table `AutoProcIntegration`
--
ALTER TABLE `AutoProcIntegration`
  ADD PRIMARY KEY (`autoProcIntegrationId`),
  ADD KEY `AutoProcIntegrationIdx1` (`dataCollectionId`),
  ADD KEY `AutoProcIntegration_FKIndex1` (`autoProcProgramId`);

--
-- Indexes for table `AutoProcProgram`
--
ALTER TABLE `AutoProcProgram`
  ADD PRIMARY KEY (`autoProcProgramId`),
  ADD KEY `fk_AutoProcProgram_1_idx` (`dataCollectionId`);

--
-- Indexes for table `AutoProcProgramAttachment`
--
ALTER TABLE `AutoProcProgramAttachment`
  ADD PRIMARY KEY (`autoProcProgramAttachmentId`),
  ADD KEY `AutoProcProgramAttachmentIdx1` (`autoProcProgramId`);

--
-- Indexes for table `AutoProcScaling`
--
ALTER TABLE `AutoProcScaling`
  ADD PRIMARY KEY (`autoProcScalingId`),
  ADD KEY `AutoProcScalingFk1` (`autoProcId`),
  ADD KEY `AutoProcScalingIdx1` (`autoProcScalingId`,`autoProcId`);

--
-- Indexes for table `AutoProcScalingStatistics`
--
ALTER TABLE `AutoProcScalingStatistics`
  ADD PRIMARY KEY (`autoProcScalingStatisticsId`),
  ADD KEY `AutoProcScalingStatisticsIdx1` (`autoProcScalingId`),
  ADD KEY `AutoProcScalingStatistics_FKindexType` (`scalingStatisticsType`);

--
-- Indexes for table `AutoProcScaling_has_Int`
--
ALTER TABLE `AutoProcScaling_has_Int`
  ADD PRIMARY KEY (`autoProcScaling_has_IntId`),
  ADD KEY `AutoProcScl_has_IntIdx1` (`autoProcScalingId`),
  ADD KEY `AutoProcScal_has_IntIdx2` (`autoProcIntegrationId`),
  ADD KEY `AutoProcScalingHasInt_FKIndex3` (`autoProcScalingId`,`autoProcIntegrationId`);

--
-- Indexes for table `AutoProcStatus`
--
ALTER TABLE `AutoProcStatus`
  ADD PRIMARY KEY (`autoProcStatusId`),
  ADD KEY `AutoProcStatus_FKIndex1` (`autoProcIntegrationId`);

--
-- Indexes for table `BeamApertures`
--
ALTER TABLE `BeamApertures`
  ADD PRIMARY KEY (`beamAperturesid`),
  ADD KEY `beamapertures_FK1` (`beamlineStatsId`);

--
-- Indexes for table `BeamCalendar`
--
ALTER TABLE `BeamCalendar`
  ADD PRIMARY KEY (`beamCalendarId`);

--
-- Indexes for table `BeamCentres`
--
ALTER TABLE `BeamCentres`
  ADD PRIMARY KEY (`beamCentresid`),
  ADD KEY `beamCentres_FK1` (`beamlineStatsId`);

--
-- Indexes for table `BeamlineAction`
--
ALTER TABLE `BeamlineAction`
  ADD PRIMARY KEY (`beamlineActionId`),
  ADD KEY `BeamlineAction_ibfk1` (`sessionId`);

--
-- Indexes for table `BeamLineSetup`
--
ALTER TABLE `BeamLineSetup`
  ADD PRIMARY KEY (`beamLineSetupId`);

--
-- Indexes for table `BeamlineStats`
--
ALTER TABLE `BeamlineStats`
  ADD PRIMARY KEY (`beamlineStatsId`);

--
-- Indexes for table `BF_automationError`
--
ALTER TABLE `BF_automationError`
  ADD PRIMARY KEY (`automationErrorId`);

--
-- Indexes for table `BF_automationFault`
--
ALTER TABLE `BF_automationFault`
  ADD PRIMARY KEY (`automationFaultId`),
  ADD KEY `BF_automationFault_ibfk1` (`automationErrorId`),
  ADD KEY `BF_automationFault_ibfk2` (`containerId`);

--
-- Indexes for table `BF_component`
--
ALTER TABLE `BF_component`
  ADD PRIMARY KEY (`componentId`),
  ADD KEY `bf_component_FK1` (`systemId`);

--
-- Indexes for table `BF_component_beamline`
--
ALTER TABLE `BF_component_beamline`
  ADD PRIMARY KEY (`component_beamlineId`),
  ADD KEY `bf_component_beamline_FK1` (`componentId`);

--
-- Indexes for table `BF_fault`
--
ALTER TABLE `BF_fault`
  ADD PRIMARY KEY (`faultId`),
  ADD KEY `bf_fault_FK1` (`sessionId`),
  ADD KEY `bf_fault_FK2` (`subcomponentId`),
  ADD KEY `bf_fault_FK3` (`personId`),
  ADD KEY `bf_fault_FK4` (`assigneeId`);

--
-- Indexes for table `BF_subcomponent`
--
ALTER TABLE `BF_subcomponent`
  ADD PRIMARY KEY (`subcomponentId`),
  ADD KEY `bf_subcomponent_FK1` (`componentId`);

--
-- Indexes for table `BF_subcomponent_beamline`
--
ALTER TABLE `BF_subcomponent_beamline`
  ADD PRIMARY KEY (`subcomponent_beamlineId`),
  ADD KEY `bf_subcomponent_beamline_FK1` (`subcomponentId`);

--
-- Indexes for table `BF_system`
--
ALTER TABLE `BF_system`
  ADD PRIMARY KEY (`systemId`);

--
-- Indexes for table `BF_system_beamline`
--
ALTER TABLE `BF_system_beamline`
  ADD PRIMARY KEY (`system_beamlineId`),
  ADD KEY `bf_system_beamline_FK1` (`systemId`);

--
-- Indexes for table `BLSample`
--
ALTER TABLE `BLSample`
  ADD PRIMARY KEY (`blSampleId`),
  ADD KEY `BLSample_FKIndex1` (`containerId`),
  ADD KEY `BLSample_FKIndex2` (`crystalId`),
  ADD KEY `BLSample_FKIndex3` (`diffractionPlanId`),
  ADD KEY `crystalId` (`crystalId`,`containerId`),
  ADD KEY `BLSample_Index1` (`name`) USING BTREE,
  ADD KEY `BLSample_FKIndex_Status` (`blSampleStatus`),
  ADD KEY `BLSampleImage_idx1` (`blSubSampleId`),
  ADD KEY `BLSample_fk5` (`screenComponentGroupId`);

--
-- Indexes for table `BLSampleGroup`
--
ALTER TABLE `BLSampleGroup`
  ADD PRIMARY KEY (`blSampleGroupId`);

--
-- Indexes for table `BLSampleGroup_has_BLSample`
--
ALTER TABLE `BLSampleGroup_has_BLSample`
  ADD PRIMARY KEY (`blSampleGroupId`,`blSampleId`),
  ADD KEY `BLSampleGroup_has_BLSample_ibfk2` (`blSampleId`);

--
-- Indexes for table `BLSampleImage`
--
ALTER TABLE `BLSampleImage`
  ADD PRIMARY KEY (`blSampleImageId`),
  ADD KEY `BLSampleImage_fk2` (`containerInspectionId`),
  ADD KEY `BLSampleImage_idx1` (`blSampleId`);

--
-- Indexes for table `BLSampleImageAnalysis`
--
ALTER TABLE `BLSampleImageAnalysis`
  ADD PRIMARY KEY (`blSampleImageAnalysisId`),
  ADD KEY `BLSampleImageAnalysis_ibfk1` (`blSampleImageId`);

--
-- Indexes for table `BLSampleImageAutoScoreClass`
--
ALTER TABLE `BLSampleImageAutoScoreClass`
  ADD PRIMARY KEY (`blSampleImageAutoScoreClassId`),
  ADD KEY `ix_BLSampleImageAutoScoreClass_blSampleImageAutoScoreSchemaId` (`blSampleImageAutoScoreSchemaId`);

--
-- Indexes for table `BLSampleImageAutoScoreSchema`
--
ALTER TABLE `BLSampleImageAutoScoreSchema`
  ADD PRIMARY KEY (`blSampleImageAutoScoreSchemaId`);

--
-- Indexes for table `BLSampleImageScore`
--
ALTER TABLE `BLSampleImageScore`
  ADD PRIMARY KEY (`blSampleImageScoreId`);

--
-- Indexes for table `BLSampleType`
--
ALTER TABLE `BLSampleType`
  ADD PRIMARY KEY (`blSampleTypeId`);

--
-- Indexes for table `BLSampleType_has_Component`
--
ALTER TABLE `BLSampleType_has_Component`
  ADD PRIMARY KEY (`blSampleTypeId`,`componentId`),
  ADD KEY `blSampleType_has_Component_fk2` (`componentId`);

--
-- Indexes for table `BLSample_has_DiffractionPlan`
--
ALTER TABLE `BLSample_has_DiffractionPlan`
  ADD PRIMARY KEY (`blSampleId`,`diffractionPlanId`),
  ADD KEY `BLSample_has_DiffractionPlan_ibfk2` (`diffractionPlanId`);

--
-- Indexes for table `BLSample_has_EnergyScan`
--
ALTER TABLE `BLSample_has_EnergyScan`
  ADD PRIMARY KEY (`blSampleHasEnergyScanId`),
  ADD KEY `BLSample_has_EnergyScan_FKIndex1` (`blSampleId`),
  ADD KEY `BLSample_has_EnergyScan_FKIndex2` (`energyScanId`);

--
-- Indexes for table `BLSession`
--
ALTER TABLE `BLSession`
  ADD PRIMARY KEY (`sessionId`),
  ADD KEY `Session_FKIndex1` (`proposalId`),
  ADD KEY `Session_FKIndex2` (`beamLineSetupId`),
  ADD KEY `Session_FKIndexStartDate` (`startDate`),
  ADD KEY `Session_FKIndexEndDate` (`endDate`),
  ADD KEY `Session_FKIndexBeamLineName` (`beamLineName`),
  ADD KEY `BLSession_FKIndexOperatorSiteNumber` (`operatorSiteNumber`),
  ADD KEY `BLSession_ibfk_3` (`beamCalendarId`);

--
-- Indexes for table `BLSession_has_SCPosition`
--
ALTER TABLE `BLSession_has_SCPosition`
  ADD PRIMARY KEY (`blsessionhasscpositionid`),
  ADD KEY `blsession_has_scposition_FK1` (`blsessionid`);

--
-- Indexes for table `BLSubSample`
--
ALTER TABLE `BLSubSample`
  ADD PRIMARY KEY (`blSubSampleId`),
  ADD KEY `BLSubSample_FKIndex1` (`blSampleId`),
  ADD KEY `BLSubSample_FKIndex2` (`diffractionPlanId`),
  ADD KEY `BLSubSample_FKIndex3` (`positionId`),
  ADD KEY `BLSubSample_FKIndex5` (`position2Id`),
  ADD KEY `BLSubSample_motorPositionfk_1` (`motorPositionId`);

--
-- Indexes for table `Buffer`
--
ALTER TABLE `Buffer`
  ADD PRIMARY KEY (`bufferId`),
  ADD KEY `BufferToSafetyLevel` (`safetyLevelId`);

--
-- Indexes for table `BufferHasAdditive`
--
ALTER TABLE `BufferHasAdditive`
  ADD PRIMARY KEY (`bufferHasAdditiveId`),
  ADD KEY `BufferHasAdditiveToBuffer` (`bufferId`),
  ADD KEY `BufferHasAdditiveToAdditive` (`additiveId`),
  ADD KEY `BufferHasAdditiveToUnit` (`measurementUnitId`);

--
-- Indexes for table `CalendarHash`
--
ALTER TABLE `CalendarHash`
  ADD PRIMARY KEY (`calendarHashId`);

--
-- Indexes for table `ComponentSubType`
--
ALTER TABLE `ComponentSubType`
  ADD PRIMARY KEY (`componentSubTypeId`);

--
-- Indexes for table `ComponentType`
--
ALTER TABLE `ComponentType`
  ADD PRIMARY KEY (`componentTypeId`);

--
-- Indexes for table `Component_has_SubType`
--
ALTER TABLE `Component_has_SubType`
  ADD PRIMARY KEY (`componentId`,`componentSubTypeId`),
  ADD KEY `component_has_SubType_fk2` (`componentSubTypeId`);

--
-- Indexes for table `ConcentrationType`
--
ALTER TABLE `ConcentrationType`
  ADD PRIMARY KEY (`concentrationTypeId`);

--
-- Indexes for table `Container`
--
ALTER TABLE `Container`
  ADD PRIMARY KEY (`containerId`),
  ADD UNIQUE KEY `Container_UNIndex1` (`barcode`),
  ADD KEY `Container_FKIndex1` (`dewarId`),
  ADD KEY `Container_FKIndex` (`beamlineLocation`),
  ADD KEY `Container_FKIndexStatus` (`containerStatus`),
  ADD KEY `Container_ibfk6` (`sessionId`),
  ADD KEY `Container_ibfk5` (`ownerId`);

--
-- Indexes for table `ContainerHistory`
--
ALTER TABLE `ContainerHistory`
  ADD PRIMARY KEY (`containerHistoryId`),
  ADD KEY `ContainerHistory_ibfk1` (`containerId`);

--
-- Indexes for table `ContainerInspection`
--
ALTER TABLE `ContainerInspection`
  ADD PRIMARY KEY (`containerInspectionId`),
  ADD KEY `ContainerInspection_fk4` (`scheduleComponentid`),
  ADD KEY `ContainerInspection_idx1` (`containerId`),
  ADD KEY `ContainerInspection_idx2` (`inspectionTypeId`),
  ADD KEY `ContainerInspection_idx3` (`imagerId`);

--
-- Indexes for table `ContainerQueue`
--
ALTER TABLE `ContainerQueue`
  ADD PRIMARY KEY (`containerQueueId`),
  ADD KEY `ContainerQueue_ibfk1` (`containerId`),
  ADD KEY `ContainerQueue_ibfk2` (`personId`);

--
-- Indexes for table `ContainerQueueSample`
--
ALTER TABLE `ContainerQueueSample`
  ADD PRIMARY KEY (`containerQueueSampleId`),
  ADD KEY `ContainerQueueSample_ibfk1` (`containerQueueId`),
  ADD KEY `ContainerQueueSample_ibfk2` (`blSubSampleId`);

--
-- Indexes for table `ContainerRegistry`
--
ALTER TABLE `ContainerRegistry`
  ADD PRIMARY KEY (`containerRegistryId`);

--
-- Indexes for table `ContainerType`
--
ALTER TABLE `ContainerType`
  ADD PRIMARY KEY (`containerTypeId`);

--
-- Indexes for table `CryoemInitialModel`
--
ALTER TABLE `CryoemInitialModel`
  ADD PRIMARY KEY (`cryoemInitialModelId`);

--
-- Indexes for table `Crystal`
--
ALTER TABLE `Crystal`
  ADD PRIMARY KEY (`crystalId`),
  ADD KEY `Crystal_FKIndex1` (`proteinId`),
  ADD KEY `Crystal_FKIndex2` (`diffractionPlanId`);

--
-- Indexes for table `Crystal_has_UUID`
--
ALTER TABLE `Crystal_has_UUID`
  ADD PRIMARY KEY (`crystal_has_UUID_Id`),
  ADD KEY `Crystal_has_UUID_FKIndex1` (`crystalId`),
  ADD KEY `Crystal_has_UUID_FKIndex2` (`UUID`);

--
-- Indexes for table `CTF`
--
ALTER TABLE `CTF`
  ADD PRIMARY KEY (`CTFid`),
  ADD KEY `fk_CTF_1_idx` (`motionCorrectionId`);

--
-- Indexes for table `DataAcquisition`
--
ALTER TABLE `DataAcquisition`
  ADD PRIMARY KEY (`dataAcquisitionId`);

--
-- Indexes for table `DataCollection`
--
ALTER TABLE `DataCollection`
  ADD PRIMARY KEY (`dataCollectionId`),
  ADD KEY `DataCollection_FKIndex1` (`dataCollectionGroupId`),
  ADD KEY `DataCollection_FKIndex2` (`strategySubWedgeOrigId`),
  ADD KEY `DataCollection_FKIndex3` (`detectorId`),
  ADD KEY `DataCollection_FKIndexStartTime` (`startTime`),
  ADD KEY `DataCollection_FKIndexImageDirectory` (`imageDirectory`),
  ADD KEY `DataCollection_FKIndexDCNumber` (`dataCollectionNumber`),
  ADD KEY `DataCollection_FKIndexImagePrefix` (`imagePrefix`),
  ADD KEY `startPositionId` (`startPositionId`),
  ADD KEY `endPositionId` (`endPositionId`),
  ADD KEY `blSubSampleId` (`blSubSampleId`);

--
-- Indexes for table `DataCollectionFileAttachment`
--
ALTER TABLE `DataCollectionFileAttachment`
  ADD PRIMARY KEY (`dataCollectionFileAttachmentId`),
  ADD KEY `dataCollectionFileAttachmentId_fk1` (`dataCollectionId`);

--
-- Indexes for table `DataCollectionGroup`
--
ALTER TABLE `DataCollectionGroup`
  ADD PRIMARY KEY (`dataCollectionGroupId`),
  ADD KEY `DataCollectionGroup_FKIndex1` (`blSampleId`),
  ADD KEY `DataCollectionGroup_FKIndex2` (`sessionId`),
  ADD KEY `workflowId` (`workflowId`);

--
-- Indexes for table `DataCollectionPlanGroup`
--
ALTER TABLE `DataCollectionPlanGroup`
  ADD PRIMARY KEY (`dataCollectionPlanGroupId`),
  ADD KEY `DataCollectionPlanGroup_ibfk1` (`sessionId`),
  ADD KEY `DataCollectionPlanGroup_ibfk2` (`blSampleId`);

--
-- Indexes for table `DatamatrixInSampleChanger`
--
ALTER TABLE `DatamatrixInSampleChanger`
  ADD PRIMARY KEY (`datamatrixInSampleChangerId`),
  ADD KEY `DatamatrixInSampleChanger_FKIndex1` (`proposalId`);

--
-- Indexes for table `DataReductionStatus`
--
ALTER TABLE `DataReductionStatus`
  ADD PRIMARY KEY (`dataReductionStatusId`);

--
-- Indexes for table `Detector`
--
ALTER TABLE `Detector`
  ADD PRIMARY KEY (`detectorId`),
  ADD UNIQUE KEY `Detector_ibuk1` (`detectorSerialNumber`),
  ADD KEY `Detector_FKIndex1` (`detectorType`,`detectorManufacturer`,`detectorModel`,`detectorPixelSizeHorizontal`,`detectorPixelSizeVertical`);

--
-- Indexes for table `Dewar`
--
ALTER TABLE `Dewar`
  ADD PRIMARY KEY (`dewarId`),
  ADD UNIQUE KEY `barCode` (`barCode`),
  ADD KEY `Dewar_FKIndex1` (`shippingId`),
  ADD KEY `Dewar_FKIndex2` (`firstExperimentId`),
  ADD KEY `Dewar_FKIndexStatus` (`dewarStatus`),
  ADD KEY `Dewar_FKIndexCode` (`code`);

--
-- Indexes for table `DewarLocation`
--
ALTER TABLE `DewarLocation`
  ADD PRIMARY KEY (`eventId`);

--
-- Indexes for table `DewarLocationList`
--
ALTER TABLE `DewarLocationList`
  ADD PRIMARY KEY (`locationId`);

--
-- Indexes for table `DewarRegistry`
--
ALTER TABLE `DewarRegistry`
  ADD PRIMARY KEY (`dewarRegistryId`),
  ADD UNIQUE KEY `facilityCode` (`facilityCode`),
  ADD KEY `DewarRegistry_ibfk_1` (`proposalId`),
  ADD KEY `DewarRegistry_ibfk_2` (`labContactId`);

--
-- Indexes for table `DewarRegistry_has_Proposal`
--
ALTER TABLE `DewarRegistry_has_Proposal`
  ADD PRIMARY KEY (`dewarRegistryHasProposalId`),
  ADD UNIQUE KEY `dewarRegistryId` (`dewarRegistryId`,`proposalId`),
  ADD KEY `DewarRegistry_has_Proposal_ibfk2` (`proposalId`),
  ADD KEY `DewarRegistry_has_Proposal_ibfk3` (`personId`),
  ADD KEY `DewarRegistry_has_Proposal_ibfk4` (`labContactId`);

--
-- Indexes for table `DewarTransportHistory`
--
ALTER TABLE `DewarTransportHistory`
  ADD PRIMARY KEY (`DewarTransportHistoryId`),
  ADD KEY `DewarTransportHistory_FKIndex1` (`dewarId`);

--
-- Indexes for table `DiffractionPlan`
--
ALTER TABLE `DiffractionPlan`
  ADD PRIMARY KEY (`diffractionPlanId`);

--
-- Indexes for table `DiffractionPlan_has_Detector`
--
ALTER TABLE `DiffractionPlan_has_Detector`
  ADD PRIMARY KEY (`diffractionPlanId`,`detectorId`),
  ADD KEY `DiffractionPlan_has_Detector_ibfk2` (`detectorId`);

--
-- Indexes for table `EMMicroscope`
--
ALTER TABLE `EMMicroscope`
  ADD PRIMARY KEY (`emMicroscopeId`);

--
-- Indexes for table `EnergyScan`
--
ALTER TABLE `EnergyScan`
  ADD PRIMARY KEY (`energyScanId`),
  ADD KEY `EnergyScan_FKIndex2` (`sessionId`),
  ADD KEY `ES_ibfk_2` (`blSampleId`),
  ADD KEY `ES_ibfk_3` (`blSubSampleId`);

--
-- Indexes for table `Experiment`
--
ALTER TABLE `Experiment`
  ADD PRIMARY KEY (`experimentId`),
  ADD KEY `fk_Experiment_To_session_idx` (`sessionId`);

--
-- Indexes for table `ExperimentKindDetails`
--
ALTER TABLE `ExperimentKindDetails`
  ADD PRIMARY KEY (`experimentKindId`),
  ADD KEY `ExperimentKindDetails_FKIndex1` (`diffractionPlanId`);

--
-- Indexes for table `ExperimentType`
--
ALTER TABLE `ExperimentType`
  ADD PRIMARY KEY (`experimentTypeId`);

--
-- Indexes for table `FitStructureToExperimentalData`
--
ALTER TABLE `FitStructureToExperimentalData`
  ADD PRIMARY KEY (`fitStructureToExperimentalDataId`),
  ADD KEY `fk_FitStructureToExperimentalData_1` (`structureId`),
  ADD KEY `fk_FitStructureToExperimentalData_2` (`workflowId`),
  ADD KEY `fk_FitStructureToExperimentalData_3` (`subtractionId`);

--
-- Indexes for table `Frame`
--
ALTER TABLE `Frame`
  ADD PRIMARY KEY (`frameId`),
  ADD KEY `FILE` (`filePath`);

--
-- Indexes for table `FrameList`
--
ALTER TABLE `FrameList`
  ADD PRIMARY KEY (`frameListId`);

--
-- Indexes for table `FrameSet`
--
ALTER TABLE `FrameSet`
  ADD PRIMARY KEY (`frameSetId`),
  ADD KEY `FramesetToRun` (`runId`),
  ADD KEY `FrameSetToFrameList` (`frameListId`);

--
-- Indexes for table `FrameToList`
--
ALTER TABLE `FrameToList`
  ADD PRIMARY KEY (`frameToListId`),
  ADD KEY `FrameToLisToFrameList` (`frameListId`),
  ADD KEY `FrameToListToFrame` (`frameId`);

--
-- Indexes for table `GeometryClassname`
--
ALTER TABLE `GeometryClassname`
  ADD PRIMARY KEY (`geometryClassnameId`);

--
-- Indexes for table `GridInfo`
--
ALTER TABLE `GridInfo`
  ADD PRIMARY KEY (`gridInfoId`),
  ADD KEY `workflowMeshId` (`workflowMeshId`),
  ADD KEY `GridInfo_ibfk_2` (`dataCollectionGroupId`);

--
-- Indexes for table `Image`
--
ALTER TABLE `Image`
  ADD PRIMARY KEY (`imageId`),
  ADD KEY `Image_FKIndex1` (`dataCollectionId`),
  ADD KEY `Image_FKIndex2` (`imageNumber`),
  ADD KEY `Image_Index3` (`fileLocation`,`fileName`) USING BTREE,
  ADD KEY `motorPositionId` (`motorPositionId`);

--
-- Indexes for table `ImageQualityIndicators`
--
ALTER TABLE `ImageQualityIndicators`
  ADD PRIMARY KEY (`imageQualityIndicatorsId`),
  ADD KEY `ImageQualityIndicatorsIdx1` (`imageId`),
  ADD KEY `AutoProcProgramIdx1` (`autoProcProgramId`);

--
-- Indexes for table `Imager`
--
ALTER TABLE `Imager`
  ADD PRIMARY KEY (`imagerId`);

--
-- Indexes for table `InputParameterWorkflow`
--
ALTER TABLE `InputParameterWorkflow`
  ADD PRIMARY KEY (`inputParameterId`);

--
-- Indexes for table `InspectionType`
--
ALTER TABLE `InspectionType`
  ADD PRIMARY KEY (`inspectionTypeId`);

--
-- Indexes for table `Instruction`
--
ALTER TABLE `Instruction`
  ADD PRIMARY KEY (`instructionId`),
  ADD KEY `InstructionToInstructionSet` (`instructionSetId`);

--
-- Indexes for table `InstructionSet`
--
ALTER TABLE `InstructionSet`
  ADD PRIMARY KEY (`instructionSetId`);

--
-- Indexes for table `IspybAutoProcAttachment`
--
ALTER TABLE `IspybAutoProcAttachment`
  ADD PRIMARY KEY (`autoProcAttachmentId`);

--
-- Indexes for table `IspybCrystalClass`
--
ALTER TABLE `IspybCrystalClass`
  ADD PRIMARY KEY (`crystalClassId`);

--
-- Indexes for table `IspybReference`
--
ALTER TABLE `IspybReference`
  ADD PRIMARY KEY (`referenceId`);

--
-- Indexes for table `LabContact`
--
ALTER TABLE `LabContact`
  ADD PRIMARY KEY (`labContactId`),
  ADD UNIQUE KEY `personAndProposal` (`personId`,`proposalId`),
  ADD UNIQUE KEY `cardNameAndProposal` (`cardName`,`proposalId`),
  ADD KEY `LabContact_FKIndex1` (`proposalId`);

--
-- Indexes for table `Laboratory`
--
ALTER TABLE `Laboratory`
  ADD PRIMARY KEY (`laboratoryId`);

--
-- Indexes for table `Log4Stat`
--
ALTER TABLE `Log4Stat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Login`
--
ALTER TABLE `Login`
  ADD PRIMARY KEY (`loginId`),
  ADD KEY `Token` (`token`);

--
-- Indexes for table `Macromolecule`
--
ALTER TABLE `Macromolecule`
  ADD PRIMARY KEY (`macromoleculeId`),
  ADD KEY `MacromoleculeToSafetyLevel` (`safetyLevelId`);

--
-- Indexes for table `MacromoleculeRegion`
--
ALTER TABLE `MacromoleculeRegion`
  ADD PRIMARY KEY (`macromoleculeRegionId`),
  ADD KEY `MacromoleculeRegionInformationToMacromolecule` (`macromoleculeId`);

--
-- Indexes for table `Measurement`
--
ALTER TABLE `Measurement`
  ADD PRIMARY KEY (`measurementId`),
  ADD KEY `SpecimenToSamplePlateWell` (`specimenId`),
  ADD KEY `MeasurementToRun` (`runId`);

--
-- Indexes for table `MeasurementToDataCollection`
--
ALTER TABLE `MeasurementToDataCollection`
  ADD PRIMARY KEY (`measurementToDataCollectionId`),
  ADD KEY `MeasurementToDataCollectionToDataCollection` (`dataCollectionId`),
  ADD KEY `MeasurementToDataCollectionToMeasurement` (`measurementId`);

--
-- Indexes for table `MeasurementUnit`
--
ALTER TABLE `MeasurementUnit`
  ADD PRIMARY KEY (`measurementUnitId`);

--
-- Indexes for table `Merge`
--
ALTER TABLE `Merge`
  ADD PRIMARY KEY (`mergeId`),
  ADD KEY `MergeToMeasurement` (`measurementId`),
  ADD KEY `MergeToListOfFrames` (`frameListId`);

--
-- Indexes for table `MixtureToStructure`
--
ALTER TABLE `MixtureToStructure`
  ADD PRIMARY KEY (`fitToStructureId`),
  ADD KEY `fk_FitToStructure_1` (`structureId`),
  ADD KEY `fk_FitToStructure_2` (`mixtureId`);

--
-- Indexes for table `Model`
--
ALTER TABLE `Model`
  ADD PRIMARY KEY (`modelId`);

--
-- Indexes for table `ModelBuilding`
--
ALTER TABLE `ModelBuilding`
  ADD PRIMARY KEY (`modelBuildingId`),
  ADD KEY `ModelBuilding_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `ModelBuilding_FKIndex2` (`phasingProgramRunId`),
  ADD KEY `ModelBuilding_FKIndex3` (`spaceGroupId`);

--
-- Indexes for table `ModelList`
--
ALTER TABLE `ModelList`
  ADD PRIMARY KEY (`modelListId`);

--
-- Indexes for table `ModelToList`
--
ALTER TABLE `ModelToList`
  ADD PRIMARY KEY (`modelToListId`),
  ADD KEY `ModelToListToList` (`modelListId`),
  ADD KEY `ModelToListToModel` (`modelId`);

--
-- Indexes for table `MotionCorrection`
--
ALTER TABLE `MotionCorrection`
  ADD PRIMARY KEY (`motionCorrectionId`),
  ADD KEY `fk_MotionCorrection_1_idx` (`movieId`),
  ADD KEY `movieId` (`movieId`);

--
-- Indexes for table `MotorPosition`
--
ALTER TABLE `MotorPosition`
  ADD PRIMARY KEY (`motorPositionId`);

--
-- Indexes for table `Movie`
--
ALTER TABLE `Movie`
  ADD PRIMARY KEY (`movieId`),
  ADD KEY `dataCollectionToMovie_idx` (`dataCollectionId`),
  ADD KEY `movieFullPath_idx` (`movieFullPath`);

--
-- Indexes for table `MXMRRun`
--
ALTER TABLE `MXMRRun`
  ADD PRIMARY KEY (`mxMRRunId`),
  ADD KEY `mxMRRun_FK1` (`autoProcScalingId`);

--
-- Indexes for table `MXMRRunBlob`
--
ALTER TABLE `MXMRRunBlob`
  ADD PRIMARY KEY (`mxMRRunBlobId`),
  ADD KEY `mxMRRunBlob_FK1` (`mxMRRunId`);

--
-- Indexes for table `Particle`
--
ALTER TABLE `Particle`
  ADD PRIMARY KEY (`particleId`),
  ADD KEY `Particle_FKIND1` (`dataCollectionId`);

--
-- Indexes for table `ParticleClassification`
--
ALTER TABLE `ParticleClassification`
  ADD PRIMARY KEY (`particleClassificationId`),
  ADD KEY `ParticleClassification_fk_particleClassificationGroupId` (`particleClassificationGroupId`);

--
-- Indexes for table `ParticleClassificationGroup`
--
ALTER TABLE `ParticleClassificationGroup`
  ADD PRIMARY KEY (`particleClassificationGroupId`),
  ADD KEY `ParticleClassificationGroup_fk_particlePickerId` (`particlePickerId`),
  ADD KEY `ParticleClassificationGroup_fk_programId` (`programId`);

--
-- Indexes for table `ParticleClassification_has_CryoemInitialModel`
--
ALTER TABLE `ParticleClassification_has_CryoemInitialModel`
  ADD PRIMARY KEY (`particleClassificationId`,`cryoemInitialModelId`),
  ADD KEY `ParticleClassification_has_InitialModel_fk2` (`cryoemInitialModelId`);

--
-- Indexes for table `ParticlePicker`
--
ALTER TABLE `ParticlePicker`
  ADD PRIMARY KEY (`particlePickerId`),
  ADD KEY `ParticlePicker_fk_programId` (`programId`),
  ADD KEY `ParticlePicker_fk_motionCorrectionId` (`firstMotionCorrectionId`);

--
-- Indexes for table `PDB`
--
ALTER TABLE `PDB`
  ADD PRIMARY KEY (`pdbId`);

--
-- Indexes for table `PDBEntry`
--
ALTER TABLE `PDBEntry`
  ADD PRIMARY KEY (`pdbEntryId`),
  ADD KEY `pdbEntryIdx1` (`autoProcProgramId`);

--
-- Indexes for table `PDBEntry_has_AutoProcProgram`
--
ALTER TABLE `PDBEntry_has_AutoProcProgram`
  ADD PRIMARY KEY (`pdbEntryHasAutoProcId`),
  ADD KEY `pdbEntry_AutoProcProgramIdx1` (`pdbEntryId`),
  ADD KEY `pdbEntry_AutoProcProgramIdx2` (`autoProcProgramId`);

--
-- Indexes for table `Permission`
--
ALTER TABLE `Permission`
  ADD PRIMARY KEY (`permissionId`);

--
-- Indexes for table `Person`
--
ALTER TABLE `Person`
  ADD PRIMARY KEY (`personId`),
  ADD KEY `Person_FKIndex1` (`laboratoryId`),
  ADD KEY `Person_FKIndexFamilyName` (`familyName`),
  ADD KEY `Person_FKIndex_Login` (`login`),
  ADD KEY `siteId` (`siteId`);

--
-- Indexes for table `Phasing`
--
ALTER TABLE `Phasing`
  ADD PRIMARY KEY (`phasingId`),
  ADD KEY `Phasing_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `Phasing_FKIndex2` (`phasingProgramRunId`),
  ADD KEY `Phasing_FKIndex3` (`spaceGroupId`);

--
-- Indexes for table `PhasingAnalysis`
--
ALTER TABLE `PhasingAnalysis`
  ADD PRIMARY KEY (`phasingAnalysisId`);

--
-- Indexes for table `PhasingProgramAttachment`
--
ALTER TABLE `PhasingProgramAttachment`
  ADD PRIMARY KEY (`phasingProgramAttachmentId`),
  ADD KEY `PhasingProgramAttachment_FKIndex1` (`phasingProgramRunId`);

--
-- Indexes for table `PhasingProgramRun`
--
ALTER TABLE `PhasingProgramRun`
  ADD PRIMARY KEY (`phasingProgramRunId`);

--
-- Indexes for table `PhasingStatistics`
--
ALTER TABLE `PhasingStatistics`
  ADD PRIMARY KEY (`phasingStatisticsId`),
  ADD KEY `PhasingStatistics_FKIndex1` (`phasingHasScalingId1`),
  ADD KEY `PhasingStatistics_FKIndex2` (`phasingHasScalingId2`),
  ADD KEY `fk_PhasingStatistics_phasingStep_idx` (`phasingStepId`);

--
-- Indexes for table `PhasingStep`
--
ALTER TABLE `PhasingStep`
  ADD PRIMARY KEY (`phasingStepId`),
  ADD KEY `FK_programRun_id` (`programRunId`),
  ADD KEY `FK_spacegroup_id` (`spaceGroupId`),
  ADD KEY `FK_autoprocScaling_id` (`autoProcScalingId`),
  ADD KEY `FK_phasingAnalysis_id` (`phasingAnalysisId`);

--
-- Indexes for table `Phasing_has_Scaling`
--
ALTER TABLE `Phasing_has_Scaling`
  ADD PRIMARY KEY (`phasingHasScalingId`),
  ADD KEY `PhasingHasScaling_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `PhasingHasScaling_FKIndex2` (`autoProcScalingId`);

--
-- Indexes for table `PHPSession`
--
ALTER TABLE `PHPSession`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `PlateGroup`
--
ALTER TABLE `PlateGroup`
  ADD PRIMARY KEY (`plateGroupId`);

--
-- Indexes for table `PlateType`
--
ALTER TABLE `PlateType`
  ADD PRIMARY KEY (`PlateTypeId`),
  ADD KEY `PlateTypeToExperiment` (`experimentId`);

--
-- Indexes for table `Position`
--
ALTER TABLE `Position`
  ADD PRIMARY KEY (`positionId`),
  ADD KEY `Position_FKIndex1` (`relativePositionId`);

--
-- Indexes for table `Positioner`
--
ALTER TABLE `Positioner`
  ADD PRIMARY KEY (`positionerId`);

--
-- Indexes for table `PreparePhasingData`
--
ALTER TABLE `PreparePhasingData`
  ADD PRIMARY KEY (`preparePhasingDataId`),
  ADD KEY `PreparePhasingData_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `PreparePhasingData_FKIndex2` (`phasingProgramRunId`),
  ADD KEY `PreparePhasingData_FKIndex3` (`spaceGroupId`);

--
-- Indexes for table `ProcessingPipeline`
--
ALTER TABLE `ProcessingPipeline`
  ADD PRIMARY KEY (`processingPipelineId`),
  ADD KEY `ix_ProcessingPipeline_processingPipelineCategoryId` (`processingPipelineCategoryId`);

--
-- Indexes for table `ProcessingPipelineCategory`
--
ALTER TABLE `ProcessingPipelineCategory`
  ADD PRIMARY KEY (`processingPipelineCategoryId`);

--
-- Indexes for table `Project`
--
ALTER TABLE `Project`
  ADD PRIMARY KEY (`projectId`),
  ADD KEY `Project_FK1` (`personId`);

--
-- Indexes for table `Project_has_BLSample`
--
ALTER TABLE `Project_has_BLSample`
  ADD PRIMARY KEY (`projectId`,`blSampleId`),
  ADD KEY `Project_has_BLSample_FK2` (`blSampleId`);

--
-- Indexes for table `Project_has_DCGroup`
--
ALTER TABLE `Project_has_DCGroup`
  ADD PRIMARY KEY (`projectId`,`dataCollectionGroupId`),
  ADD KEY `Project_has_DCGroup_FK2` (`dataCollectionGroupId`);

--
-- Indexes for table `Project_has_EnergyScan`
--
ALTER TABLE `Project_has_EnergyScan`
  ADD PRIMARY KEY (`projectId`,`energyScanId`),
  ADD KEY `project_has_energyscan_FK2` (`energyScanId`);

--
-- Indexes for table `Project_has_Person`
--
ALTER TABLE `Project_has_Person`
  ADD PRIMARY KEY (`projectId`,`personId`),
  ADD KEY `project_has_person_FK2` (`personId`);

--
-- Indexes for table `Project_has_Protein`
--
ALTER TABLE `Project_has_Protein`
  ADD PRIMARY KEY (`projectId`,`proteinId`),
  ADD KEY `project_has_protein_FK2` (`proteinId`);

--
-- Indexes for table `Project_has_Session`
--
ALTER TABLE `Project_has_Session`
  ADD PRIMARY KEY (`projectId`,`sessionId`),
  ADD KEY `project_has_session_FK2` (`sessionId`);

--
-- Indexes for table `Project_has_Shipping`
--
ALTER TABLE `Project_has_Shipping`
  ADD PRIMARY KEY (`projectId`,`shippingId`),
  ADD KEY `project_has_shipping_FK2` (`shippingId`);

--
-- Indexes for table `Project_has_User`
--
ALTER TABLE `Project_has_User`
  ADD PRIMARY KEY (`projecthasuserid`),
  ADD KEY `Project_Has_user_FK1` (`projectid`);

--
-- Indexes for table `Project_has_XFEFSpectrum`
--
ALTER TABLE `Project_has_XFEFSpectrum`
  ADD PRIMARY KEY (`projectId`,`xfeFluorescenceSpectrumId`),
  ADD KEY `project_has_xfefspectrum_FK2` (`xfeFluorescenceSpectrumId`);

--
-- Indexes for table `Proposal`
--
ALTER TABLE `Proposal`
  ADD PRIMARY KEY (`proposalId`),
  ADD KEY `Proposal_FKIndex1` (`personId`),
  ADD KEY `Proposal_FKIndexCodeNumber` (`proposalCode`,`proposalNumber`);

--
-- Indexes for table `ProposalHasPerson`
--
ALTER TABLE `ProposalHasPerson`
  ADD PRIMARY KEY (`proposalId`,`personId`),
  ADD UNIQUE KEY `uq_proposal_person` (`proposalId`,`personId`),
  ADD KEY `fk_ProposalHasPerson_Proposal` (`proposalId`),
  ADD KEY `fk_ProposalHasPerson_Personal` (`personId`);

--
-- Indexes for table `Protein`
--
ALTER TABLE `Protein`
  ADD PRIMARY KEY (`proteinId`),
  ADD KEY `Protein_FKIndex1` (`proposalId`),
  ADD KEY `ProteinAcronym_Index` (`proposalId`,`acronym`),
  ADD KEY `Protein_FKIndex2` (`personId`),
  ADD KEY `Protein_Index2` (`acronym`) USING BTREE,
  ADD KEY `protein_fk3` (`componentTypeId`);

--
-- Indexes for table `Protein_has_Lattice`
--
ALTER TABLE `Protein_has_Lattice`
  ADD PRIMARY KEY (`proteinId`);

--
-- Indexes for table `Protein_has_PDB`
--
ALTER TABLE `Protein_has_PDB`
  ADD PRIMARY KEY (`proteinhaspdbid`),
  ADD KEY `Protein_Has_PDB_fk1` (`proteinid`),
  ADD KEY `Protein_Has_PDB_fk2` (`pdbid`);

--
-- Indexes for table `PurificationColumn`
--
ALTER TABLE `PurificationColumn`
  ADD PRIMARY KEY (`purificationColumnId`);

--
-- Indexes for table `RigidBodyModeling`
--
ALTER TABLE `RigidBodyModeling`
  ADD PRIMARY KEY (`rigidBodyModelingId`),
  ADD KEY `fk_RigidBodyModeling_1` (`subtractionId`);

--
-- Indexes for table `RobotAction`
--
ALTER TABLE `RobotAction`
  ADD PRIMARY KEY (`robotActionId`),
  ADD KEY `RobotAction_FK1` (`blsessionId`),
  ADD KEY `RobotAction_FK2` (`blsampleId`);

--
-- Indexes for table `Run`
--
ALTER TABLE `Run`
  ADD PRIMARY KEY (`runId`);

--
-- Indexes for table `SafetyLevel`
--
ALTER TABLE `SafetyLevel`
  ADD PRIMARY KEY (`safetyLevelId`);

--
-- Indexes for table `SAMPLECELL`
--
ALTER TABLE `SAMPLECELL`
  ADD PRIMARY KEY (`SAMPLECELLID`);

--
-- Indexes for table `SAMPLEEXPOSUREUNIT`
--
ALTER TABLE `SAMPLEEXPOSUREUNIT`
  ADD PRIMARY KEY (`SAMPLEEXPOSUREUNITID`);

--
-- Indexes for table `SamplePlate`
--
ALTER TABLE `SamplePlate`
  ADD PRIMARY KEY (`samplePlateId`),
  ADD KEY `PlateToPtateGroup` (`plateGroupId`),
  ADD KEY `SamplePlateToType` (`plateTypeId`),
  ADD KEY `SamplePlateToExperiment` (`experimentId`),
  ADD KEY `SamplePlateToInstructionSet` (`instructionSetId`);

--
-- Indexes for table `SamplePlatePosition`
--
ALTER TABLE `SamplePlatePosition`
  ADD PRIMARY KEY (`samplePlatePositionId`),
  ADD KEY `PlatePositionToPlate` (`samplePlateId`);

--
-- Indexes for table `SaxsDataCollection`
--
ALTER TABLE `SaxsDataCollection`
  ADD PRIMARY KEY (`dataCollectionId`),
  ADD KEY `SaxsDataCollectionToExperiment` (`experimentId`);

--
-- Indexes for table `SAXSDATACOLLECTIONGROUP`
--
ALTER TABLE `SAXSDATACOLLECTIONGROUP`
  ADD PRIMARY KEY (`DATACOLLECTIONGROUPID`);

--
-- Indexes for table `ScanParametersModel`
--
ALTER TABLE `ScanParametersModel`
  ADD PRIMARY KEY (`scanParametersModelId`),
  ADD KEY `PDF_Model_ibfk1` (`scanParametersServiceId`),
  ADD KEY `PDF_Model_ibfk2` (`dataCollectionPlanId`);

--
-- Indexes for table `ScanParametersService`
--
ALTER TABLE `ScanParametersService`
  ADD PRIMARY KEY (`scanParametersServiceId`);

--
-- Indexes for table `Schedule`
--
ALTER TABLE `Schedule`
  ADD PRIMARY KEY (`scheduleId`);

--
-- Indexes for table `ScheduleComponent`
--
ALTER TABLE `ScheduleComponent`
  ADD PRIMARY KEY (`scheduleComponentId`),
  ADD KEY `ScheduleComponent_fk2` (`inspectionTypeId`),
  ADD KEY `ScheduleComponent_idx1` (`scheduleId`);

--
-- Indexes for table `SchemaStatus`
--
ALTER TABLE `SchemaStatus`
  ADD PRIMARY KEY (`schemaStatusId`),
  ADD UNIQUE KEY `scriptName` (`scriptName`);

--
-- Indexes for table `Screen`
--
ALTER TABLE `Screen`
  ADD PRIMARY KEY (`screenId`),
  ADD KEY `Screen_fk1` (`proposalId`);

--
-- Indexes for table `ScreenComponent`
--
ALTER TABLE `ScreenComponent`
  ADD PRIMARY KEY (`screenComponentId`),
  ADD KEY `ScreenComponent_fk1` (`screenComponentGroupId`),
  ADD KEY `ScreenComponent_fk2` (`componentId`);

--
-- Indexes for table `ScreenComponentGroup`
--
ALTER TABLE `ScreenComponentGroup`
  ADD PRIMARY KEY (`screenComponentGroupId`),
  ADD KEY `ScreenComponentGroup_fk1` (`screenId`);

--
-- Indexes for table `Screening`
--
ALTER TABLE `Screening`
  ADD PRIMARY KEY (`screeningId`),
  ADD KEY `Screening_FKIndexDiffractionPlanId` (`diffractionPlanId`),
  ADD KEY `dcgroupId` (`dataCollectionGroupId`);

--
-- Indexes for table `ScreeningInput`
--
ALTER TABLE `ScreeningInput`
  ADD PRIMARY KEY (`screeningInputId`),
  ADD KEY `ScreeningInput_FKIndex1` (`screeningId`);

--
-- Indexes for table `ScreeningOutput`
--
ALTER TABLE `ScreeningOutput`
  ADD PRIMARY KEY (`screeningOutputId`),
  ADD KEY `ScreeningOutput_FKIndex1` (`screeningId`);

--
-- Indexes for table `ScreeningOutputLattice`
--
ALTER TABLE `ScreeningOutputLattice`
  ADD PRIMARY KEY (`screeningOutputLatticeId`),
  ADD KEY `ScreeningOutputLattice_FKIndex1` (`screeningOutputId`);

--
-- Indexes for table `ScreeningRank`
--
ALTER TABLE `ScreeningRank`
  ADD PRIMARY KEY (`screeningRankId`),
  ADD KEY `ScreeningRank_FKIndex1` (`screeningId`),
  ADD KEY `ScreeningRank_FKIndex2` (`screeningRankSetId`);

--
-- Indexes for table `ScreeningRankSet`
--
ALTER TABLE `ScreeningRankSet`
  ADD PRIMARY KEY (`screeningRankSetId`);

--
-- Indexes for table `ScreeningStrategy`
--
ALTER TABLE `ScreeningStrategy`
  ADD PRIMARY KEY (`screeningStrategyId`),
  ADD KEY `ScreeningStrategy_FKIndex1` (`screeningOutputId`);

--
-- Indexes for table `ScreeningStrategySubWedge`
--
ALTER TABLE `ScreeningStrategySubWedge`
  ADD PRIMARY KEY (`screeningStrategySubWedgeId`),
  ADD KEY `ScreeningStrategySubWedge_FK1` (`screeningStrategyWedgeId`);

--
-- Indexes for table `ScreeningStrategyWedge`
--
ALTER TABLE `ScreeningStrategyWedge`
  ADD PRIMARY KEY (`screeningStrategyWedgeId`),
  ADD KEY `ScreeningStrategyWedge_IBFK_1` (`screeningStrategyId`);

--
-- Indexes for table `SessionType`
--
ALTER TABLE `SessionType`
  ADD PRIMARY KEY (`sessionTypeId`),
  ADD KEY `SessionType_FKIndex1` (`sessionId`);

--
-- Indexes for table `Session_has_Person`
--
ALTER TABLE `Session_has_Person`
  ADD PRIMARY KEY (`sessionId`,`personId`),
  ADD KEY `Session_has_Person_FKIndex1` (`sessionId`),
  ADD KEY `Session_has_Person_FKIndex2` (`personId`);

--
-- Indexes for table `Shipping`
--
ALTER TABLE `Shipping`
  ADD PRIMARY KEY (`shippingId`),
  ADD KEY `Shipping_FKIndex1` (`proposalId`),
  ADD KEY `laboratoryId` (`laboratoryId`),
  ADD KEY `Shipping_FKIndex2` (`sendingLabContactId`),
  ADD KEY `Shipping_FKIndex3` (`returnLabContactId`),
  ADD KEY `Shipping_FKIndexCreationDate` (`creationDate`),
  ADD KEY `Shipping_FKIndexName` (`shippingName`),
  ADD KEY `Shipping_FKIndexStatus` (`shippingStatus`);

--
-- Indexes for table `ShippingHasSession`
--
ALTER TABLE `ShippingHasSession`
  ADD PRIMARY KEY (`shippingId`,`sessionId`),
  ADD KEY `ShippingHasSession_FKIndex1` (`shippingId`),
  ADD KEY `ShippingHasSession_FKIndex2` (`sessionId`);

--
-- Indexes for table `Sleeve`
--
ALTER TABLE `Sleeve`
  ADD PRIMARY KEY (`sleeveId`);

--
-- Indexes for table `SpaceGroup`
--
ALTER TABLE `SpaceGroup`
  ADD PRIMARY KEY (`spaceGroupId`),
  ADD KEY `SpaceGroup_FKShortName` (`spaceGroupShortName`),
  ADD KEY `geometryClassnameId` (`geometryClassnameId`);

--
-- Indexes for table `Specimen`
--
ALTER TABLE `Specimen`
  ADD PRIMARY KEY (`specimenId`),
  ADD KEY `SamplePlateWellToBuffer` (`bufferId`),
  ADD KEY `SamplePlateWellToMacromolecule` (`macromoleculeId`),
  ADD KEY `SamplePlateWellToSamplePlatePosition` (`samplePlatePositionId`),
  ADD KEY `SamplePlateWellToSafetyLevel` (`safetyLevelId`),
  ADD KEY `SamplePlateWellToExperiment` (`experimentId`),
  ADD KEY `SampleToStockSolution` (`stockSolutionId`);

--
-- Indexes for table `StockSolution`
--
ALTER TABLE `StockSolution`
  ADD PRIMARY KEY (`stockSolutionId`),
  ADD KEY `StockSolutionToBuffer` (`bufferId`),
  ADD KEY `StockSolutionToMacromolecule` (`macromoleculeId`),
  ADD KEY `StockSolutionToInstructionSet` (`instructionSetId`);

--
-- Indexes for table `Stoichiometry`
--
ALTER TABLE `Stoichiometry`
  ADD PRIMARY KEY (`stoichiometryId`),
  ADD KEY `StoichiometryToHost` (`hostMacromoleculeId`),
  ADD KEY `StoichiometryToMacromolecule` (`macromoleculeId`);

--
-- Indexes for table `Structure`
--
ALTER TABLE `Structure`
  ADD PRIMARY KEY (`structureId`),
  ADD KEY `StructureToMacromolecule` (`macromoleculeId`),
  ADD KEY `StructureToCrystal_idx` (`crystalId`),
  ADD KEY `StructureToBlSample_idx` (`blSampleId`),
  ADD KEY `StructureToProposal_idx` (`proposalId`);

--
-- Indexes for table `SubstructureDetermination`
--
ALTER TABLE `SubstructureDetermination`
  ADD PRIMARY KEY (`substructureDeterminationId`),
  ADD KEY `SubstructureDetermination_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `SubstructureDetermination_FKIndex2` (`phasingProgramRunId`),
  ADD KEY `SubstructureDetermination_FKIndex3` (`spaceGroupId`);

--
-- Indexes for table `Subtraction`
--
ALTER TABLE `Subtraction`
  ADD PRIMARY KEY (`subtractionId`),
  ADD KEY `EdnaAnalysisToMeasurement` (`dataCollectionId`),
  ADD KEY `fk_Subtraction_1` (`sampleOneDimensionalFiles`),
  ADD KEY `fk_Subtraction_2` (`bufferOnedimensionalFiles`);

--
-- Indexes for table `SubtractionToAbInitioModel`
--
ALTER TABLE `SubtractionToAbInitioModel`
  ADD PRIMARY KEY (`subtractionToAbInitioModelId`),
  ADD KEY `substractionToAbInitioModelToAbinitioModel` (`abInitioId`),
  ADD KEY `ubstractionToSubstraction` (`subtractionId`);

--
-- Indexes for table `Superposition`
--
ALTER TABLE `Superposition`
  ADD PRIMARY KEY (`superpositionId`),
  ADD KEY `fk_Superposition_1` (`subtractionId`);

--
-- Indexes for table `SW_onceToken`
--
ALTER TABLE `SW_onceToken`
  ADD PRIMARY KEY (`onceTokenId`),
  ADD KEY `SW_onceToken_fk1` (`personId`),
  ADD KEY `SW_onceToken_fk2` (`proposalId`);

--
-- Indexes for table `UntrustedRegion`
--
ALTER TABLE `UntrustedRegion`
  ADD PRIMARY KEY (`untrustedRegionId`),
  ADD KEY `UntrustedRegion_FKIndex1` (`detectorId`);

--
-- Indexes for table `UserGroup`
--
ALTER TABLE `UserGroup`
  ADD PRIMARY KEY (`userGroupId`),
  ADD UNIQUE KEY `UserGroup_idx1` (`name`);

--
-- Indexes for table `UserGroup_has_Permission`
--
ALTER TABLE `UserGroup_has_Permission`
  ADD PRIMARY KEY (`userGroupId`,`permissionId`),
  ADD KEY `UserGroup_has_Permission_fk2` (`permissionId`);

--
-- Indexes for table `UserGroup_has_Person`
--
ALTER TABLE `UserGroup_has_Person`
  ADD PRIMARY KEY (`userGroupId`,`personId`),
  ADD KEY `userGroup_has_Person_fk2` (`personId`);

--
-- Indexes for table `v_run`
--
ALTER TABLE `v_run`
  ADD PRIMARY KEY (`runId`),
  ADD KEY `v_run_idx1` (`startDate`,`endDate`);

--
-- Indexes for table `Workflow`
--
ALTER TABLE `Workflow`
  ADD PRIMARY KEY (`workflowId`);

--
-- Indexes for table `WorkflowDehydration`
--
ALTER TABLE `WorkflowDehydration`
  ADD PRIMARY KEY (`workflowDehydrationId`),
  ADD KEY `WorkflowDehydration_FKIndex1` (`workflowId`);

--
-- Indexes for table `WorkflowMesh`
--
ALTER TABLE `WorkflowMesh`
  ADD PRIMARY KEY (`workflowMeshId`),
  ADD KEY `WorkflowMesh_FKIndex1` (`workflowId`),
  ADD KEY `bestPositionId` (`bestPositionId`),
  ADD KEY `bestImageId` (`bestImageId`);

--
-- Indexes for table `WorkflowStep`
--
ALTER TABLE `WorkflowStep`
  ADD PRIMARY KEY (`workflowStepId`),
  ADD KEY `step_to_workflow_fk_idx` (`workflowId`);

--
-- Indexes for table `WorkflowType`
--
ALTER TABLE `WorkflowType`
  ADD PRIMARY KEY (`workflowTypeId`);

--
-- Indexes for table `XFEFluorescenceSpectrum`
--
ALTER TABLE `XFEFluorescenceSpectrum`
  ADD PRIMARY KEY (`xfeFluorescenceSpectrumId`),
  ADD KEY `XFEFluorescnceSpectrum_FKIndex1` (`blSampleId`),
  ADD KEY `XFEFluorescnceSpectrum_FKIndex2` (`sessionId`),
  ADD KEY `XFE_ibfk_3` (`blSubSampleId`);

--
-- Indexes for table `XRFFluorescenceMapping`
--
ALTER TABLE `XRFFluorescenceMapping`
  ADD PRIMARY KEY (`xrfFluorescenceMappingId`),
  ADD KEY `XRFFluorescenceMapping_ibfk1` (`xrfFluorescenceMappingROIId`),
  ADD KEY `XRFFluorescenceMapping_ibfk2` (`dataCollectionId`);

--
-- Indexes for table `XRFFluorescenceMappingROI`
--
ALTER TABLE `XRFFluorescenceMappingROI`
  ADD PRIMARY KEY (`xrfFluorescenceMappingROIId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `AbInitioModel`
--
ALTER TABLE `AbInitioModel`
  MODIFY `abInitioModelId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Additive`
--
ALTER TABLE `Additive`
  MODIFY `additiveId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `AdminActivity`
--
ALTER TABLE `AdminActivity`
  MODIFY `adminActivityId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `AdminVar`
--
ALTER TABLE `AdminVar`
  MODIFY `varId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Aperture`
--
ALTER TABLE `Aperture`
  MODIFY `apertureId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Assembly`
--
ALTER TABLE `Assembly`
  MODIFY `assemblyId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `AssemblyHasMacromolecule`
--
ALTER TABLE `AssemblyHasMacromolecule`
  MODIFY `AssemblyHasMacromoleculeId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `AssemblyRegion`
--
ALTER TABLE `AssemblyRegion`
  MODIFY `assemblyRegionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `AutoProc`
--
ALTER TABLE `AutoProc`
  MODIFY `autoProcId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `AutoProcIntegration`
--
ALTER TABLE `AutoProcIntegration`
  MODIFY `autoProcIntegrationId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `AutoProcProgram`
--
ALTER TABLE `AutoProcProgram`
  MODIFY `autoProcProgramId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `AutoProcProgramAttachment`
--
ALTER TABLE `AutoProcProgramAttachment`
  MODIFY `autoProcProgramAttachmentId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `AutoProcScaling`
--
ALTER TABLE `AutoProcScaling`
  MODIFY `autoProcScalingId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `AutoProcScalingStatistics`
--
ALTER TABLE `AutoProcScalingStatistics`
  MODIFY `autoProcScalingStatisticsId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `AutoProcScaling_has_Int`
--
ALTER TABLE `AutoProcScaling_has_Int`
  MODIFY `autoProcScaling_has_IntId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `AutoProcStatus`
--
ALTER TABLE `AutoProcStatus`
  MODIFY `autoProcStatusId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `BeamApertures`
--
ALTER TABLE `BeamApertures`
  MODIFY `beamAperturesid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BeamCalendar`
--
ALTER TABLE `BeamCalendar`
  MODIFY `beamCalendarId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BeamCentres`
--
ALTER TABLE `BeamCentres`
  MODIFY `beamCentresid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BeamlineAction`
--
ALTER TABLE `BeamlineAction`
  MODIFY `beamlineActionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BeamLineSetup`
--
ALTER TABLE `BeamLineSetup`
  MODIFY `beamLineSetupId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BeamlineStats`
--
ALTER TABLE `BeamlineStats`
  MODIFY `beamlineStatsId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_automationError`
--
ALTER TABLE `BF_automationError`
  MODIFY `automationErrorId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_automationFault`
--
ALTER TABLE `BF_automationFault`
  MODIFY `automationFaultId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_component`
--
ALTER TABLE `BF_component`
  MODIFY `componentId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_component_beamline`
--
ALTER TABLE `BF_component_beamline`
  MODIFY `component_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_fault`
--
ALTER TABLE `BF_fault`
  MODIFY `faultId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_subcomponent`
--
ALTER TABLE `BF_subcomponent`
  MODIFY `subcomponentId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_subcomponent_beamline`
--
ALTER TABLE `BF_subcomponent_beamline`
  MODIFY `subcomponent_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_system`
--
ALTER TABLE `BF_system`
  MODIFY `systemId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BF_system_beamline`
--
ALTER TABLE `BF_system_beamline`
  MODIFY `system_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSample`
--
ALTER TABLE `BLSample`
  MODIFY `blSampleId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSampleGroup`
--
ALTER TABLE `BLSampleGroup`
  MODIFY `blSampleGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSampleImage`
--
ALTER TABLE `BLSampleImage`
  MODIFY `blSampleImageId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSampleImageAnalysis`
--
ALTER TABLE `BLSampleImageAnalysis`
  MODIFY `blSampleImageAnalysisId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSampleImageAutoScoreClass`
--
ALTER TABLE `BLSampleImageAutoScoreClass`
  MODIFY `blSampleImageAutoScoreClassId` tinyint(3) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSampleImageAutoScoreSchema`
--
ALTER TABLE `BLSampleImageAutoScoreSchema`
  MODIFY `blSampleImageAutoScoreSchemaId` tinyint(3) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSampleImageScore`
--
ALTER TABLE `BLSampleImageScore`
  MODIFY `blSampleImageScoreId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSampleType`
--
ALTER TABLE `BLSampleType`
  MODIFY `blSampleTypeId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSample_has_EnergyScan`
--
ALTER TABLE `BLSample_has_EnergyScan`
  MODIFY `blSampleHasEnergyScanId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSession`
--
ALTER TABLE `BLSession`
  MODIFY `sessionId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSession_has_SCPosition`
--
ALTER TABLE `BLSession_has_SCPosition`
  MODIFY `blsessionhasscpositionid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BLSubSample`
--
ALTER TABLE `BLSubSample`
  MODIFY `blSubSampleId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `Buffer`
--
ALTER TABLE `Buffer`
  MODIFY `bufferId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `BufferHasAdditive`
--
ALTER TABLE `BufferHasAdditive`
  MODIFY `bufferHasAdditiveId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `CalendarHash`
--
ALTER TABLE `CalendarHash`
  MODIFY `calendarHashId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ComponentType`
--
ALTER TABLE `ComponentType`
  MODIFY `componentTypeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ConcentrationType`
--
ALTER TABLE `ConcentrationType`
  MODIFY `concentrationTypeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Container`
--
ALTER TABLE `Container`
  MODIFY `containerId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ContainerHistory`
--
ALTER TABLE `ContainerHistory`
  MODIFY `containerHistoryId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ContainerInspection`
--
ALTER TABLE `ContainerInspection`
  MODIFY `containerInspectionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ContainerQueue`
--
ALTER TABLE `ContainerQueue`
  MODIFY `containerQueueId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ContainerQueueSample`
--
ALTER TABLE `ContainerQueueSample`
  MODIFY `containerQueueSampleId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ContainerRegistry`
--
ALTER TABLE `ContainerRegistry`
  MODIFY `containerRegistryId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ContainerType`
--
ALTER TABLE `ContainerType`
  MODIFY `containerTypeId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `CryoemInitialModel`
--
ALTER TABLE `CryoemInitialModel`
  MODIFY `cryoemInitialModelId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Crystal`
--
ALTER TABLE `Crystal`
  MODIFY `crystalId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Crystal_has_UUID`
--
ALTER TABLE `Crystal_has_UUID`
  MODIFY `crystal_has_UUID_Id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `CTF`
--
ALTER TABLE `CTF`
  MODIFY `CTFid` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DataAcquisition`
--
ALTER TABLE `DataAcquisition`
  MODIFY `dataAcquisitionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DataCollection`
--
ALTER TABLE `DataCollection`
  MODIFY `dataCollectionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `DataCollectionFileAttachment`
--
ALTER TABLE `DataCollectionFileAttachment`
  MODIFY `dataCollectionFileAttachmentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DataCollectionGroup`
--
ALTER TABLE `DataCollectionGroup`
  MODIFY `dataCollectionGroupId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `DataCollectionPlanGroup`
--
ALTER TABLE `DataCollectionPlanGroup`
  MODIFY `dataCollectionPlanGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DatamatrixInSampleChanger`
--
ALTER TABLE `DatamatrixInSampleChanger`
  MODIFY `datamatrixInSampleChangerId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DataReductionStatus`
--
ALTER TABLE `DataReductionStatus`
  MODIFY `dataReductionStatusId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Detector`
--
ALTER TABLE `Detector`
  MODIFY `detectorId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `Dewar`
--
ALTER TABLE `Dewar`
  MODIFY `dewarId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DewarLocation`
--
ALTER TABLE `DewarLocation`
  MODIFY `eventId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DewarLocationList`
--
ALTER TABLE `DewarLocationList`
  MODIFY `locationId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DewarRegistry`
--
ALTER TABLE `DewarRegistry`
  MODIFY `dewarRegistryId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DewarRegistry_has_Proposal`
--
ALTER TABLE `DewarRegistry_has_Proposal`
  MODIFY `dewarRegistryHasProposalId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DewarTransportHistory`
--
ALTER TABLE `DewarTransportHistory`
  MODIFY `DewarTransportHistoryId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `DiffractionPlan`
--
ALTER TABLE `DiffractionPlan`
  MODIFY `diffractionPlanId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `EMMicroscope`
--
ALTER TABLE `EMMicroscope`
  MODIFY `emMicroscopeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `EnergyScan`
--
ALTER TABLE `EnergyScan`
  MODIFY `energyScanId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Experiment`
--
ALTER TABLE `Experiment`
  MODIFY `experimentId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ExperimentKindDetails`
--
ALTER TABLE `ExperimentKindDetails`
  MODIFY `experimentKindId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ExperimentType`
--
ALTER TABLE `ExperimentType`
  MODIFY `experimentTypeId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `FitStructureToExperimentalData`
--
ALTER TABLE `FitStructureToExperimentalData`
  MODIFY `fitStructureToExperimentalDataId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Frame`
--
ALTER TABLE `Frame`
  MODIFY `frameId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `FrameList`
--
ALTER TABLE `FrameList`
  MODIFY `frameListId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `FrameSet`
--
ALTER TABLE `FrameSet`
  MODIFY `frameSetId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `FrameToList`
--
ALTER TABLE `FrameToList`
  MODIFY `frameToListId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `GeometryClassname`
--
ALTER TABLE `GeometryClassname`
  MODIFY `geometryClassnameId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `GridInfo`
--
ALTER TABLE `GridInfo`
  MODIFY `gridInfoId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `Image`
--
ALTER TABLE `Image`
  MODIFY `imageId` int(12) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ImageQualityIndicators`
--
ALTER TABLE `ImageQualityIndicators`
  MODIFY `imageQualityIndicatorsId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `Imager`
--
ALTER TABLE `Imager`
  MODIFY `imagerId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `InputParameterWorkflow`
--
ALTER TABLE `InputParameterWorkflow`
  MODIFY `inputParameterId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `InspectionType`
--
ALTER TABLE `InspectionType`
  MODIFY `inspectionTypeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Instruction`
--
ALTER TABLE `Instruction`
  MODIFY `instructionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `InstructionSet`
--
ALTER TABLE `InstructionSet`
  MODIFY `instructionSetId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `IspybAutoProcAttachment`
--
ALTER TABLE `IspybAutoProcAttachment`
  MODIFY `autoProcAttachmentId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `IspybCrystalClass`
--
ALTER TABLE `IspybCrystalClass`
  MODIFY `crystalClassId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `IspybReference`
--
ALTER TABLE `IspybReference`
  MODIFY `referenceId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `LabContact`
--
ALTER TABLE `LabContact`
  MODIFY `labContactId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Laboratory`
--
ALTER TABLE `Laboratory`
  MODIFY `laboratoryId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Log4Stat`
--
ALTER TABLE `Log4Stat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Login`
--
ALTER TABLE `Login`
  MODIFY `loginId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Macromolecule`
--
ALTER TABLE `Macromolecule`
  MODIFY `macromoleculeId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MacromoleculeRegion`
--
ALTER TABLE `MacromoleculeRegion`
  MODIFY `macromoleculeRegionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Measurement`
--
ALTER TABLE `Measurement`
  MODIFY `measurementId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MeasurementToDataCollection`
--
ALTER TABLE `MeasurementToDataCollection`
  MODIFY `measurementToDataCollectionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Merge`
--
ALTER TABLE `Merge`
  MODIFY `mergeId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MixtureToStructure`
--
ALTER TABLE `MixtureToStructure`
  MODIFY `fitToStructureId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Model`
--
ALTER TABLE `Model`
  MODIFY `modelId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ModelBuilding`
--
ALTER TABLE `ModelBuilding`
  MODIFY `modelBuildingId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `ModelList`
--
ALTER TABLE `ModelList`
  MODIFY `modelListId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ModelToList`
--
ALTER TABLE `ModelToList`
  MODIFY `modelToListId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MotionCorrection`
--
ALTER TABLE `MotionCorrection`
  MODIFY `motionCorrectionId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MotorPosition`
--
ALTER TABLE `MotorPosition`
  MODIFY `motorPositionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `Movie`
--
ALTER TABLE `Movie`
  MODIFY `movieId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MXMRRun`
--
ALTER TABLE `MXMRRun`
  MODIFY `mxMRRunId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `MXMRRunBlob`
--
ALTER TABLE `MXMRRunBlob`
  MODIFY `mxMRRunBlobId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ParticleClassification`
--
ALTER TABLE `ParticleClassification`
  MODIFY `particleClassificationId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ParticleClassificationGroup`
--
ALTER TABLE `ParticleClassificationGroup`
  MODIFY `particleClassificationGroupId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ParticlePicker`
--
ALTER TABLE `ParticlePicker`
  MODIFY `particlePickerId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `PDB`
--
ALTER TABLE `PDB`
  MODIFY `pdbId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `PDBEntry`
--
ALTER TABLE `PDBEntry`
  MODIFY `pdbEntryId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `PDBEntry_has_AutoProcProgram`
--
ALTER TABLE `PDBEntry_has_AutoProcProgram`
  MODIFY `pdbEntryHasAutoProcId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Permission`
--
ALTER TABLE `Permission`
  MODIFY `permissionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Person`
--
ALTER TABLE `Person`
  MODIFY `personId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Phasing`
--
ALTER TABLE `Phasing`
  MODIFY `phasingId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `PhasingAnalysis`
--
ALTER TABLE `PhasingAnalysis`
  MODIFY `phasingAnalysisId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `PhasingProgramAttachment`
--
ALTER TABLE `PhasingProgramAttachment`
  MODIFY `phasingProgramAttachmentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `PhasingProgramRun`
--
ALTER TABLE `PhasingProgramRun`
  MODIFY `phasingProgramRunId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `PhasingStatistics`
--
ALTER TABLE `PhasingStatistics`
  MODIFY `phasingStatisticsId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `PhasingStep`
--
ALTER TABLE `PhasingStep`
  MODIFY `phasingStepId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Phasing_has_Scaling`
--
ALTER TABLE `Phasing_has_Scaling`
  MODIFY `phasingHasScalingId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `PlateGroup`
--
ALTER TABLE `PlateGroup`
  MODIFY `plateGroupId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `PlateType`
--
ALTER TABLE `PlateType`
  MODIFY `PlateTypeId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Position`
--
ALTER TABLE `Position`
  MODIFY `positionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `Positioner`
--
ALTER TABLE `Positioner`
  MODIFY `positionerId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `PreparePhasingData`
--
ALTER TABLE `PreparePhasingData`
  MODIFY `preparePhasingDataId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `ProcessingPipeline`
--
ALTER TABLE `ProcessingPipeline`
  MODIFY `processingPipelineId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ProcessingPipelineCategory`
--
ALTER TABLE `ProcessingPipelineCategory`
  MODIFY `processingPipelineCategoryId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Project`
--
ALTER TABLE `Project`
  MODIFY `projectId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Project_has_User`
--
ALTER TABLE `Project_has_User`
  MODIFY `projecthasuserid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Proposal`
--
ALTER TABLE `Proposal`
  MODIFY `proposalId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Protein`
--
ALTER TABLE `Protein`
  MODIFY `proteinId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Protein_has_PDB`
--
ALTER TABLE `Protein_has_PDB`
  MODIFY `proteinhaspdbid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `PurificationColumn`
--
ALTER TABLE `PurificationColumn`
  MODIFY `purificationColumnId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `RigidBodyModeling`
--
ALTER TABLE `RigidBodyModeling`
  MODIFY `rigidBodyModelingId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `RobotAction`
--
ALTER TABLE `RobotAction`
  MODIFY `robotActionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Run`
--
ALTER TABLE `Run`
  MODIFY `runId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SafetyLevel`
--
ALTER TABLE `SafetyLevel`
  MODIFY `safetyLevelId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SAMPLECELL`
--
ALTER TABLE `SAMPLECELL`
  MODIFY `SAMPLECELLID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SAMPLEEXPOSUREUNIT`
--
ALTER TABLE `SAMPLEEXPOSUREUNIT`
  MODIFY `SAMPLEEXPOSUREUNITID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SamplePlate`
--
ALTER TABLE `SamplePlate`
  MODIFY `samplePlateId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SamplePlatePosition`
--
ALTER TABLE `SamplePlatePosition`
  MODIFY `samplePlatePositionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SaxsDataCollection`
--
ALTER TABLE `SaxsDataCollection`
  MODIFY `dataCollectionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SAXSDATACOLLECTIONGROUP`
--
ALTER TABLE `SAXSDATACOLLECTIONGROUP`
  MODIFY `DATACOLLECTIONGROUPID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScanParametersModel`
--
ALTER TABLE `ScanParametersModel`
  MODIFY `scanParametersModelId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScanParametersService`
--
ALTER TABLE `ScanParametersService`
  MODIFY `scanParametersServiceId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Schedule`
--
ALTER TABLE `Schedule`
  MODIFY `scheduleId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScheduleComponent`
--
ALTER TABLE `ScheduleComponent`
  MODIFY `scheduleComponentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SchemaStatus`
--
ALTER TABLE `SchemaStatus`
  MODIFY `schemaStatusId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Screen`
--
ALTER TABLE `Screen`
  MODIFY `screenId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreenComponent`
--
ALTER TABLE `ScreenComponent`
  MODIFY `screenComponentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreenComponentGroup`
--
ALTER TABLE `ScreenComponentGroup`
  MODIFY `screenComponentGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Screening`
--
ALTER TABLE `Screening`
  MODIFY `screeningId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreeningInput`
--
ALTER TABLE `ScreeningInput`
  MODIFY `screeningInputId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreeningOutput`
--
ALTER TABLE `ScreeningOutput`
  MODIFY `screeningOutputId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreeningOutputLattice`
--
ALTER TABLE `ScreeningOutputLattice`
  MODIFY `screeningOutputLatticeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreeningRank`
--
ALTER TABLE `ScreeningRank`
  MODIFY `screeningRankId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreeningRankSet`
--
ALTER TABLE `ScreeningRankSet`
  MODIFY `screeningRankSetId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreeningStrategy`
--
ALTER TABLE `ScreeningStrategy`
  MODIFY `screeningStrategyId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ScreeningStrategySubWedge`
--
ALTER TABLE `ScreeningStrategySubWedge`
  MODIFY `screeningStrategySubWedgeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key';

--
-- AUTO_INCREMENT for table `ScreeningStrategyWedge`
--
ALTER TABLE `ScreeningStrategyWedge`
  MODIFY `screeningStrategyWedgeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key';

--
-- AUTO_INCREMENT for table `SessionType`
--
ALTER TABLE `SessionType`
  MODIFY `sessionTypeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Shipping`
--
ALTER TABLE `Shipping`
  MODIFY `shippingId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Sleeve`
--
ALTER TABLE `Sleeve`
  MODIFY `sleeveId` tinyint(3) NOT NULL AUTO_INCREMENT COMMENT 'The unique sleeve id 1...255 which also identifies its home location in the freezer';

--
-- AUTO_INCREMENT for table `SpaceGroup`
--
ALTER TABLE `SpaceGroup`
  MODIFY `spaceGroupId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key';

--
-- AUTO_INCREMENT for table `Specimen`
--
ALTER TABLE `Specimen`
  MODIFY `specimenId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `StockSolution`
--
ALTER TABLE `StockSolution`
  MODIFY `stockSolutionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Stoichiometry`
--
ALTER TABLE `Stoichiometry`
  MODIFY `stoichiometryId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Structure`
--
ALTER TABLE `Structure`
  MODIFY `structureId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SubstructureDetermination`
--
ALTER TABLE `SubstructureDetermination`
  MODIFY `substructureDeterminationId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `Subtraction`
--
ALTER TABLE `Subtraction`
  MODIFY `subtractionId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SubtractionToAbInitioModel`
--
ALTER TABLE `SubtractionToAbInitioModel`
  MODIFY `subtractionToAbInitioModelId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Superposition`
--
ALTER TABLE `Superposition`
  MODIFY `superpositionId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SW_onceToken`
--
ALTER TABLE `SW_onceToken`
  MODIFY `onceTokenId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `UntrustedRegion`
--
ALTER TABLE `UntrustedRegion`
  MODIFY `untrustedRegionId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `UserGroup`
--
ALTER TABLE `UserGroup`
  MODIFY `userGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `v_run`
--
ALTER TABLE `v_run`
  MODIFY `runId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Workflow`
--
ALTER TABLE `Workflow`
  MODIFY `workflowId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `WorkflowDehydration`
--
ALTER TABLE `WorkflowDehydration`
  MODIFY `workflowDehydrationId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `WorkflowMesh`
--
ALTER TABLE `WorkflowMesh`
  MODIFY `workflowMeshId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';

--
-- AUTO_INCREMENT for table `WorkflowStep`
--
ALTER TABLE `WorkflowStep`
  MODIFY `workflowStepId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `WorkflowType`
--
ALTER TABLE `WorkflowType`
  MODIFY `workflowTypeId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `XFEFluorescenceSpectrum`
--
ALTER TABLE `XFEFluorescenceSpectrum`
  MODIFY `xfeFluorescenceSpectrumId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `XRFFluorescenceMapping`
--
ALTER TABLE `XRFFluorescenceMapping`
  MODIFY `xrfFluorescenceMappingId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `XRFFluorescenceMappingROI`
--
ALTER TABLE `XRFFluorescenceMappingROI`
  MODIFY `xrfFluorescenceMappingROIId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `AbInitioModel`
--
ALTER TABLE `AbInitioModel`
  ADD CONSTRAINT `AbInitioModelToModelList` FOREIGN KEY (`modelListId`) REFERENCES `ModelList` (`modelListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `AbInitioModelToRapid` FOREIGN KEY (`rapidShapeDeterminationModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `AverageToModel` FOREIGN KEY (`averagedModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SahpeDeterminationToAbiniti` FOREIGN KEY (`shapeDeterminationModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Assembly`
--
ALTER TABLE `Assembly`
  ADD CONSTRAINT `AssemblyToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `AssemblyHasMacromolecule`
--
ALTER TABLE `AssemblyHasMacromolecule`
  ADD CONSTRAINT `AssemblyHasMacromoleculeToAssembly` FOREIGN KEY (`assemblyId`) REFERENCES `Assembly` (`assemblyId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `AssemblyHasMacromoleculeToAssemblyRegion` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `AssemblyRegion`
--
ALTER TABLE `AssemblyRegion`
  ADD CONSTRAINT `AssemblyRegionToAssemblyHasMacromolecule` FOREIGN KEY (`assemblyHasMacromoleculeId`) REFERENCES `AssemblyHasMacromolecule` (`AssemblyHasMacromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `AutoProcIntegration`
--
ALTER TABLE `AutoProcIntegration`
  ADD CONSTRAINT `AutoProcIntegration_ibfk_1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `AutoProcIntegration_ibfk_2` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcProgram`
--
ALTER TABLE `AutoProcProgram`
  ADD CONSTRAINT `AutoProcProgram_FK1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `AutoProcProgramAttachment`
--
ALTER TABLE `AutoProcProgramAttachment`
  ADD CONSTRAINT `AutoProcProgramAttachmentFk1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcScaling`
--
ALTER TABLE `AutoProcScaling`
  ADD CONSTRAINT `AutoProcScalingFk1` FOREIGN KEY (`autoProcId`) REFERENCES `AutoProc` (`autoProcId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcScalingStatistics`
--
ALTER TABLE `AutoProcScalingStatistics`
  ADD CONSTRAINT `AutoProcScalingStatisticsFk1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcScaling_has_Int`
--
ALTER TABLE `AutoProcScaling_has_Int`
  ADD CONSTRAINT `AutoProcScaling_has_IntFk1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `AutoProcScaling_has_IntFk2` FOREIGN KEY (`autoProcIntegrationId`) REFERENCES `AutoProcIntegration` (`autoProcIntegrationId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcStatus`
--
ALTER TABLE `AutoProcStatus`
  ADD CONSTRAINT `AutoProcStatus_ibfk_1` FOREIGN KEY (`autoProcIntegrationId`) REFERENCES `AutoProcIntegration` (`autoProcIntegrationId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BeamApertures`
--
ALTER TABLE `BeamApertures`
  ADD CONSTRAINT `beamapertures_FK1` FOREIGN KEY (`beamlineStatsId`) REFERENCES `BeamlineStats` (`beamlineStatsId`) ON DELETE CASCADE;

--
-- Constraints for table `BeamCentres`
--
ALTER TABLE `BeamCentres`
  ADD CONSTRAINT `beamCentres_FK1` FOREIGN KEY (`beamlineStatsId`) REFERENCES `BeamlineStats` (`beamlineStatsId`) ON DELETE CASCADE;

--
-- Constraints for table `BeamlineAction`
--
ALTER TABLE `BeamlineAction`
  ADD CONSTRAINT `BeamlineAction_ibfk1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`);

--
-- Constraints for table `BF_automationFault`
--
ALTER TABLE `BF_automationFault`
  ADD CONSTRAINT `BF_automationFault_ibfk1` FOREIGN KEY (`automationErrorId`) REFERENCES `BF_automationError` (`automationErrorId`),
  ADD CONSTRAINT `BF_automationFault_ibfk2` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`);

--
-- Constraints for table `BF_component`
--
ALTER TABLE `BF_component`
  ADD CONSTRAINT `bf_component_FK1` FOREIGN KEY (`systemId`) REFERENCES `BF_system` (`systemId`);

--
-- Constraints for table `BF_component_beamline`
--
ALTER TABLE `BF_component_beamline`
  ADD CONSTRAINT `bf_component_beamline_FK1` FOREIGN KEY (`componentId`) REFERENCES `BF_component` (`componentId`);

--
-- Constraints for table `BF_fault`
--
ALTER TABLE `BF_fault`
  ADD CONSTRAINT `bf_fault_FK1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`),
  ADD CONSTRAINT `bf_fault_FK2` FOREIGN KEY (`subcomponentId`) REFERENCES `BF_subcomponent` (`subcomponentId`),
  ADD CONSTRAINT `bf_fault_FK3` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  ADD CONSTRAINT `bf_fault_FK4` FOREIGN KEY (`assigneeId`) REFERENCES `Person` (`personId`);

--
-- Constraints for table `BF_subcomponent`
--
ALTER TABLE `BF_subcomponent`
  ADD CONSTRAINT `bf_subcomponent_FK1` FOREIGN KEY (`componentId`) REFERENCES `BF_component` (`componentId`);

--
-- Constraints for table `BF_subcomponent_beamline`
--
ALTER TABLE `BF_subcomponent_beamline`
  ADD CONSTRAINT `bf_subcomponent_beamline_FK1` FOREIGN KEY (`subcomponentId`) REFERENCES `BF_subcomponent` (`subcomponentId`);

--
-- Constraints for table `BF_system_beamline`
--
ALTER TABLE `BF_system_beamline`
  ADD CONSTRAINT `bf_system_beamline_FK1` FOREIGN KEY (`systemId`) REFERENCES `BF_system` (`systemId`);

--
-- Constraints for table `BLSample`
--
ALTER TABLE `BLSample`
  ADD CONSTRAINT `BLSample_ibfk_1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSample_ibfk_2` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSample_ibfk_3` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSampleGroup_has_BLSample`
--
ALTER TABLE `BLSampleGroup_has_BLSample`
  ADD CONSTRAINT `BLSampleGroup_has_BLSample_ibfk1` FOREIGN KEY (`blSampleGroupId`) REFERENCES `BLSampleGroup` (`blSampleGroupId`),
  ADD CONSTRAINT `BLSampleGroup_has_BLSample_ibfk2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`);

--
-- Constraints for table `BLSampleImage`
--
ALTER TABLE `BLSampleImage`
  ADD CONSTRAINT `BLSampleImage_fk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSampleImage_fk2` FOREIGN KEY (`containerInspectionId`) REFERENCES `ContainerInspection` (`containerInspectionId`);

--
-- Constraints for table `BLSampleImageAnalysis`
--
ALTER TABLE `BLSampleImageAnalysis`
  ADD CONSTRAINT `BLSampleImageAnalysis_ibfk1` FOREIGN KEY (`blSampleImageId`) REFERENCES `BLSampleImage` (`blSampleImageId`);

--
-- Constraints for table `BLSampleImageAutoScoreClass`
--
ALTER TABLE `BLSampleImageAutoScoreClass`
  ADD CONSTRAINT `BLSampleImageAutoScoreClass_ibfk_1` FOREIGN KEY (`blSampleImageAutoScoreSchemaId`) REFERENCES `BLSampleImageAutoScoreSchema` (`blSampleImageAutoScoreSchemaId`) ON UPDATE CASCADE;

--
-- Constraints for table `BLSampleType_has_Component`
--
ALTER TABLE `BLSampleType_has_Component`
  ADD CONSTRAINT `blSampleType_has_Component_fk1` FOREIGN KEY (`blSampleTypeId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `blSampleType_has_Component_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSample_has_DiffractionPlan`
--
ALTER TABLE `BLSample_has_DiffractionPlan`
  ADD CONSTRAINT `BLSample_has_DiffractionPlan_ibfk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`),
  ADD CONSTRAINT `BLSample_has_DiffractionPlan_ibfk2` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`);

--
-- Constraints for table `BLSample_has_EnergyScan`
--
ALTER TABLE `BLSample_has_EnergyScan`
  ADD CONSTRAINT `BLSample_has_EnergyScan_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSample_has_EnergyScan_ibfk_2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSession`
--
ALTER TABLE `BLSession`
  ADD CONSTRAINT `BLSession_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSession_ibfk_2` FOREIGN KEY (`beamLineSetupId`) REFERENCES `BeamLineSetup` (`beamLineSetupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSession_ibfk_3` FOREIGN KEY (`beamCalendarId`) REFERENCES `BeamCalendar` (`beamCalendarId`);

--
-- Constraints for table `BLSession_has_SCPosition`
--
ALTER TABLE `BLSession_has_SCPosition`
  ADD CONSTRAINT `blsession_has_scposition_FK1` FOREIGN KEY (`blsessionid`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSubSample`
--
ALTER TABLE `BLSubSample`
  ADD CONSTRAINT `BLSubSample_blSamplefk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSubSample_diffractionPlanfk_1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSubSample_motorPositionfk_1` FOREIGN KEY (`motorPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSubSample_positionfk_1` FOREIGN KEY (`positionId`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSubSample_positionfk_2` FOREIGN KEY (`position2Id`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Buffer`
--
ALTER TABLE `Buffer`
  ADD CONSTRAINT `BufferToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `BufferHasAdditive`
--
ALTER TABLE `BufferHasAdditive`
  ADD CONSTRAINT `BufferHasAdditiveToAdditive` FOREIGN KEY (`additiveId`) REFERENCES `Additive` (`additiveId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `BufferHasAdditiveToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `BufferHasAdditiveToUnit` FOREIGN KEY (`measurementUnitId`) REFERENCES `MeasurementUnit` (`measurementUnitId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Component_has_SubType`
--
ALTER TABLE `Component_has_SubType`
  ADD CONSTRAINT `component_has_SubType_fk1` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE,
  ADD CONSTRAINT `component_has_SubType_fk2` FOREIGN KEY (`componentSubTypeId`) REFERENCES `ComponentSubType` (`componentSubTypeId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Container`
--
ALTER TABLE `Container`
  ADD CONSTRAINT `Container_ibfk5` FOREIGN KEY (`ownerId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk6` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ContainerHistory`
--
ALTER TABLE `ContainerHistory`
  ADD CONSTRAINT `ContainerHistory_ibfk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ContainerInspection`
--
ALTER TABLE `ContainerInspection`
  ADD CONSTRAINT `ContainerInspection_fk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ContainerInspection_fk2` FOREIGN KEY (`inspectionTypeId`) REFERENCES `InspectionType` (`inspectionTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ContainerInspection_fk3` FOREIGN KEY (`imagerId`) REFERENCES `Imager` (`imagerId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ContainerInspection_fk4` FOREIGN KEY (`scheduleComponentid`) REFERENCES `ScheduleComponent` (`scheduleComponentId`);

--
-- Constraints for table `ContainerQueue`
--
ALTER TABLE `ContainerQueue`
  ADD CONSTRAINT `ContainerQueue_ibfk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ContainerQueue_ibfk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON UPDATE CASCADE;

--
-- Constraints for table `ContainerQueueSample`
--
ALTER TABLE `ContainerQueueSample`
  ADD CONSTRAINT `ContainerQueueSample_ibfk1` FOREIGN KEY (`containerQueueId`) REFERENCES `ContainerQueue` (`containerQueueId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ContainerQueueSample_ibfk2` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Crystal`
--
ALTER TABLE `Crystal`
  ADD CONSTRAINT `Crystal_ibfk_1` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Crystal_ibfk_2` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Crystal_has_UUID`
--
ALTER TABLE `Crystal_has_UUID`
  ADD CONSTRAINT `ibfk_1` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DataCollection`
--
ALTER TABLE `DataCollection`
  ADD CONSTRAINT `DataCollection_ibfk_1` FOREIGN KEY (`strategySubWedgeOrigId`) REFERENCES `ScreeningStrategySubWedge` (`screeningStrategySubWedgeId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollection_ibfk_2` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollection_ibfk_3` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollection_ibfk_8` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`);

--
-- Constraints for table `DataCollectionFileAttachment`
--
ALTER TABLE `DataCollectionFileAttachment`
  ADD CONSTRAINT `dataCollectionFileAttachmentId_fk1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DataCollectionGroup`
--
ALTER TABLE `DataCollectionGroup`
  ADD CONSTRAINT `DataCollectionGroup_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollectionGroup_ibfk_2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollectionGroup_ibfk_3` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DataCollectionPlanGroup`
--
ALTER TABLE `DataCollectionPlanGroup`
  ADD CONSTRAINT `DataCollectionPlanGroup_ibfk1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollectionPlanGroup_ibfk2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON UPDATE CASCADE;

--
-- Constraints for table `Dewar`
--
ALTER TABLE `Dewar`
  ADD CONSTRAINT `Dewar_ibfk_1` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Dewar_ibfk_2` FOREIGN KEY (`firstExperimentId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DewarRegistry`
--
ALTER TABLE `DewarRegistry`
  ADD CONSTRAINT `DewarRegistry_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `DewarRegistry_ibfk_2` FOREIGN KEY (`labContactId`) REFERENCES `LabContact` (`labContactId`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `DewarRegistry_has_Proposal`
--
ALTER TABLE `DewarRegistry_has_Proposal`
  ADD CONSTRAINT `DewarRegistry_has_Proposal_ibfk1` FOREIGN KEY (`dewarRegistryId`) REFERENCES `DewarRegistry` (`dewarRegistryId`),
  ADD CONSTRAINT `DewarRegistry_has_Proposal_ibfk2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`),
  ADD CONSTRAINT `DewarRegistry_has_Proposal_ibfk3` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  ADD CONSTRAINT `DewarRegistry_has_Proposal_ibfk4` FOREIGN KEY (`labContactId`) REFERENCES `LabContact` (`labContactId`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `DewarTransportHistory`
--
ALTER TABLE `DewarTransportHistory`
  ADD CONSTRAINT `DewarTransportHistory_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DiffractionPlan_has_Detector`
--
ALTER TABLE `DiffractionPlan_has_Detector`
  ADD CONSTRAINT `DiffractionPlan_has_Detector_ibfk1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`),
  ADD CONSTRAINT `DiffractionPlan_has_Detector_ibfk2` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`);

--
-- Constraints for table `EnergyScan`
--
ALTER TABLE `EnergyScan`
  ADD CONSTRAINT `ES_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ES_ibfk_2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ES_ibfk_3` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Experiment`
--
ALTER TABLE `Experiment`
  ADD CONSTRAINT `fk_Experiment_To_session` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `ExperimentKindDetails`
--
ALTER TABLE `ExperimentKindDetails`
  ADD CONSTRAINT `EKD_ibfk_1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `FitStructureToExperimentalData`
--
ALTER TABLE `FitStructureToExperimentalData`
  ADD CONSTRAINT `fk_FitStructureToExperimentalData_1` FOREIGN KEY (`structureId`) REFERENCES `Structure` (`structureId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_FitStructureToExperimentalData_2` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_FitStructureToExperimentalData_3` FOREIGN KEY (`subtractionId`) REFERENCES `Subtraction` (`subtractionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `FrameSet`
--
ALTER TABLE `FrameSet`
  ADD CONSTRAINT `FrameSetToFrameList` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FramesetToRun` FOREIGN KEY (`runId`) REFERENCES `Run` (`runId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `FrameToList`
--
ALTER TABLE `FrameToList`
  ADD CONSTRAINT `FrameToLisToFrameList` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FrameToListToFrame` FOREIGN KEY (`frameId`) REFERENCES `Frame` (`frameId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `GridInfo`
--
ALTER TABLE `GridInfo`
  ADD CONSTRAINT `GridInfo_ibfk_1` FOREIGN KEY (`workflowMeshId`) REFERENCES `WorkflowMesh` (`workflowMeshId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `GridInfo_ibfk_2` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Image`
--
ALTER TABLE `Image`
  ADD CONSTRAINT `Image_ibfk_1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Image_ibfk_3` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `ImageQualityIndicators`
--
ALTER TABLE `ImageQualityIndicators`
  ADD CONSTRAINT `AutoProcProgramFk1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Instruction`
--
ALTER TABLE `Instruction`
  ADD CONSTRAINT `InstructionToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `LabContact`
--
ALTER TABLE `LabContact`
  ADD CONSTRAINT `LabContact_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  ADD CONSTRAINT `LabContact_ibfk_2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`);

--
-- Constraints for table `Macromolecule`
--
ALTER TABLE `Macromolecule`
  ADD CONSTRAINT `MacromoleculeToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MacromoleculeRegion`
--
ALTER TABLE `MacromoleculeRegion`
  ADD CONSTRAINT `MacromoleculeRegionInformationToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Measurement`
--
ALTER TABLE `Measurement`
  ADD CONSTRAINT `MeasurementToRun` FOREIGN KEY (`runId`) REFERENCES `Run` (`runId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SpecimenToSamplePlateWell` FOREIGN KEY (`specimenId`) REFERENCES `Specimen` (`specimenId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MeasurementToDataCollection`
--
ALTER TABLE `MeasurementToDataCollection`
  ADD CONSTRAINT `MeasurementToDataCollectionToDataCollection` FOREIGN KEY (`dataCollectionId`) REFERENCES `SaxsDataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `MeasurementToDataCollectionToMeasurement` FOREIGN KEY (`measurementId`) REFERENCES `Measurement` (`measurementId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Merge`
--
ALTER TABLE `Merge`
  ADD CONSTRAINT `MergeToListOfFrames` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `MergeToMeasurement` FOREIGN KEY (`measurementId`) REFERENCES `Measurement` (`measurementId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MixtureToStructure`
--
ALTER TABLE `MixtureToStructure`
  ADD CONSTRAINT `fk_FitToStructure_1` FOREIGN KEY (`structureId`) REFERENCES `Structure` (`structureId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_FitToStructure_2` FOREIGN KEY (`mixtureId`) REFERENCES `FitStructureToExperimentalData` (`fitStructureToExperimentalDataId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `ModelBuilding`
--
ALTER TABLE `ModelBuilding`
  ADD CONSTRAINT `ModelBuilding_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ModelBuilding_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ModelBuilding_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ModelToList`
--
ALTER TABLE `ModelToList`
  ADD CONSTRAINT `ModelToListToList` FOREIGN KEY (`modelListId`) REFERENCES `ModelList` (`modelListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ModelToListToModel` FOREIGN KEY (`modelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MotionCorrection`
--
ALTER TABLE `MotionCorrection`
  ADD CONSTRAINT `fk_MotionCorrection_1` FOREIGN KEY (`movieId`) REFERENCES `Movie` (`movieId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Movie`
--
ALTER TABLE `Movie`
  ADD CONSTRAINT `dataCollectionToMovie` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MXMRRun`
--
ALTER TABLE `MXMRRun`
  ADD CONSTRAINT `mxMRRun_FK1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`);

--
-- Constraints for table `MXMRRunBlob`
--
ALTER TABLE `MXMRRunBlob`
  ADD CONSTRAINT `mxMRRunBlob_FK1` FOREIGN KEY (`mxMRRunId`) REFERENCES `MXMRRun` (`mxMRRunId`);

--
-- Constraints for table `Particle`
--
ALTER TABLE `Particle`
  ADD CONSTRAINT `Particle_FK1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ParticleClassification`
--
ALTER TABLE `ParticleClassification`
  ADD CONSTRAINT `ParticleClassification_fk_particleClassificationGroupId` FOREIGN KEY (`particleClassificationGroupId`) REFERENCES `ParticleClassificationGroup` (`particleClassificationGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ParticleClassificationGroup`
--
ALTER TABLE `ParticleClassificationGroup`
  ADD CONSTRAINT `ParticleClassificationGroup_fk_particlePickerId` FOREIGN KEY (`particlePickerId`) REFERENCES `ParticlePicker` (`particlePickerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ParticleClassificationGroup_fk_programId` FOREIGN KEY (`programId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `ParticleClassification_has_CryoemInitialModel`
--
ALTER TABLE `ParticleClassification_has_CryoemInitialModel`
  ADD CONSTRAINT `ParticleClassification_has_CryoemInitialModel_fk1` FOREIGN KEY (`particleClassificationId`) REFERENCES `ParticleClassification` (`particleClassificationId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ParticleClassification_has_InitialModel_fk2` FOREIGN KEY (`cryoemInitialModelId`) REFERENCES `CryoemInitialModel` (`cryoemInitialModelId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ParticlePicker`
--
ALTER TABLE `ParticlePicker`
  ADD CONSTRAINT `ParticlePicker_fk_motionCorrectionId` FOREIGN KEY (`firstMotionCorrectionId`) REFERENCES `MotionCorrection` (`motionCorrectionId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `ParticlePicker_fk_programId` FOREIGN KEY (`programId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `PDBEntry`
--
ALTER TABLE `PDBEntry`
  ADD CONSTRAINT `pdbEntry_FK1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE;

--
-- Constraints for table `PDBEntry_has_AutoProcProgram`
--
ALTER TABLE `PDBEntry_has_AutoProcProgram`
  ADD CONSTRAINT `pdbEntry_AutoProcProgram_FK1` FOREIGN KEY (`pdbEntryId`) REFERENCES `PDBEntry` (`pdbEntryId`) ON DELETE CASCADE,
  ADD CONSTRAINT `pdbEntry_AutoProcProgram_FK2` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE;

--
-- Constraints for table `Person`
--
ALTER TABLE `Person`
  ADD CONSTRAINT `Person_ibfk_1` FOREIGN KEY (`laboratoryId`) REFERENCES `Laboratory` (`laboratoryId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Phasing`
--
ALTER TABLE `Phasing`
  ADD CONSTRAINT `Phasing_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Phasing_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Phasing_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PhasingProgramAttachment`
--
ALTER TABLE `PhasingProgramAttachment`
  ADD CONSTRAINT `Phasing_phasingProgramAttachmentfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PhasingStatistics`
--
ALTER TABLE `PhasingStatistics`
  ADD CONSTRAINT `PhasingStatistics_phasingHasScalingfk_1` FOREIGN KEY (`phasingHasScalingId1`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PhasingStatistics_phasingHasScalingfk_2` FOREIGN KEY (`phasingHasScalingId2`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_PhasingStatistics_phasingStep` FOREIGN KEY (`phasingStepId`) REFERENCES `PhasingStep` (`phasingStepId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `PhasingStep`
--
ALTER TABLE `PhasingStep`
  ADD CONSTRAINT `FK_autoprocScaling` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_program` FOREIGN KEY (`programRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_spacegroup` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Phasing_has_Scaling`
--
ALTER TABLE `Phasing_has_Scaling`
  ADD CONSTRAINT `PhasingHasScaling_autoProcScalingfk_1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PhasingHasScaling_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Position`
--
ALTER TABLE `Position`
  ADD CONSTRAINT `Position_relativePositionfk_1` FOREIGN KEY (`relativePositionId`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PreparePhasingData`
--
ALTER TABLE `PreparePhasingData`
  ADD CONSTRAINT `PreparePhasingData_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PreparePhasingData_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PreparePhasingData_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ProcessingPipeline`
--
ALTER TABLE `ProcessingPipeline`
  ADD CONSTRAINT `ProcessingPipeline_ibfk_1` FOREIGN KEY (`processingPipelineCategoryId`) REFERENCES `ProcessingPipelineCategory` (`processingPipelineCategoryId`);

--
-- Constraints for table `Project`
--
ALTER TABLE `Project`
  ADD CONSTRAINT `Project_FK1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`);

--
-- Constraints for table `Project_has_BLSample`
--
ALTER TABLE `Project_has_BLSample`
  ADD CONSTRAINT `Project_has_BLSample_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Project_has_BLSample_FK2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Project_has_DCGroup`
--
ALTER TABLE `Project_has_DCGroup`
  ADD CONSTRAINT `Project_has_DCGroup_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Project_has_DCGroup_FK2` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Project_has_EnergyScan`
--
ALTER TABLE `Project_has_EnergyScan`
  ADD CONSTRAINT `project_has_energyscan_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `project_has_energyscan_FK2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Project_has_Person`
--
ALTER TABLE `Project_has_Person`
  ADD CONSTRAINT `project_has_person_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  ADD CONSTRAINT `project_has_person_FK2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE;

--
-- Constraints for table `Project_has_Protein`
--
ALTER TABLE `Project_has_Protein`
  ADD CONSTRAINT `project_has_protein_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  ADD CONSTRAINT `project_has_protein_FK2` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE;

--
-- Constraints for table `Project_has_Session`
--
ALTER TABLE `Project_has_Session`
  ADD CONSTRAINT `project_has_session_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `project_has_session_FK2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Project_has_Shipping`
--
ALTER TABLE `Project_has_Shipping`
  ADD CONSTRAINT `project_has_shipping_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  ADD CONSTRAINT `project_has_shipping_FK2` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`) ON DELETE CASCADE;

--
-- Constraints for table `Project_has_User`
--
ALTER TABLE `Project_has_User`
  ADD CONSTRAINT `Project_Has_user_FK1` FOREIGN KEY (`projectid`) REFERENCES `Project` (`projectId`);

--
-- Constraints for table `Project_has_XFEFSpectrum`
--
ALTER TABLE `Project_has_XFEFSpectrum`
  ADD CONSTRAINT `project_has_xfefspectrum_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  ADD CONSTRAINT `project_has_xfefspectrum_FK2` FOREIGN KEY (`xfeFluorescenceSpectrumId`) REFERENCES `XFEFluorescenceSpectrum` (`xfeFluorescenceSpectrumId`) ON DELETE CASCADE;

--
-- Constraints for table `Proposal`
--
ALTER TABLE `Proposal`
  ADD CONSTRAINT `Proposal_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ProposalHasPerson`
--
ALTER TABLE `ProposalHasPerson`
  ADD CONSTRAINT `fk_ProposalHasPerson_Personal` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_ProposalHasPerson_Proposal` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Protein`
--
ALTER TABLE `Protein`
  ADD CONSTRAINT `Protein_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `protein_fk3` FOREIGN KEY (`componentTypeId`) REFERENCES `ComponentType` (`componentTypeId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Protein_has_Lattice`
--
ALTER TABLE `Protein_has_Lattice`
  ADD CONSTRAINT `Protein_has_Lattice_ibfk1` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`);

--
-- Constraints for table `Protein_has_PDB`
--
ALTER TABLE `Protein_has_PDB`
  ADD CONSTRAINT `Protein_Has_PDB_fk1` FOREIGN KEY (`proteinid`) REFERENCES `Protein` (`proteinId`),
  ADD CONSTRAINT `Protein_Has_PDB_fk2` FOREIGN KEY (`pdbid`) REFERENCES `PDB` (`pdbId`);

--
-- Constraints for table `RobotAction`
--
ALTER TABLE `RobotAction`
  ADD CONSTRAINT `RobotAction_FK1` FOREIGN KEY (`blsessionId`) REFERENCES `BLSession` (`sessionId`),
  ADD CONSTRAINT `RobotAction_FK2` FOREIGN KEY (`blsampleId`) REFERENCES `BLSample` (`blSampleId`);

--
-- Constraints for table `SamplePlate`
--
ALTER TABLE `SamplePlate`
  ADD CONSTRAINT `PlateToPtateGroup` FOREIGN KEY (`plateGroupId`) REFERENCES `PlateGroup` (`plateGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToType` FOREIGN KEY (`plateTypeId`) REFERENCES `PlateType` (`PlateTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SamplePlatePosition`
--
ALTER TABLE `SamplePlatePosition`
  ADD CONSTRAINT `PlatePositionToPlate` FOREIGN KEY (`samplePlateId`) REFERENCES `SamplePlate` (`samplePlateId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SaxsDataCollection`
--
ALTER TABLE `SaxsDataCollection`
  ADD CONSTRAINT `SaxsDataCollectionToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `ScanParametersModel`
--
ALTER TABLE `ScanParametersModel`
  ADD CONSTRAINT `PDF_Model_ibfk1` FOREIGN KEY (`scanParametersServiceId`) REFERENCES `ScanParametersService` (`scanParametersServiceId`) ON UPDATE CASCADE,
  ADD CONSTRAINT `PDF_Model_ibfk2` FOREIGN KEY (`dataCollectionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON UPDATE CASCADE;

--
-- Constraints for table `ScheduleComponent`
--
ALTER TABLE `ScheduleComponent`
  ADD CONSTRAINT `ScheduleComponent_fk1` FOREIGN KEY (`scheduleId`) REFERENCES `Schedule` (`scheduleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ScheduleComponent_fk2` FOREIGN KEY (`inspectionTypeId`) REFERENCES `InspectionType` (`inspectionTypeId`) ON DELETE CASCADE;

--
-- Constraints for table `Screen`
--
ALTER TABLE `Screen`
  ADD CONSTRAINT `Screen_fk1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`);

--
-- Constraints for table `ScreenComponent`
--
ALTER TABLE `ScreenComponent`
  ADD CONSTRAINT `ScreenComponent_fk1` FOREIGN KEY (`screenComponentGroupId`) REFERENCES `ScreenComponentGroup` (`screenComponentGroupId`),
  ADD CONSTRAINT `ScreenComponent_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`);

--
-- Constraints for table `ScreenComponentGroup`
--
ALTER TABLE `ScreenComponentGroup`
  ADD CONSTRAINT `ScreenComponentGroup_fk1` FOREIGN KEY (`screenId`) REFERENCES `Screen` (`screenId`);

--
-- Constraints for table `Screening`
--
ALTER TABLE `Screening`
  ADD CONSTRAINT `Screening_ibfk_1` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `ScreeningInput`
--
ALTER TABLE `ScreeningInput`
  ADD CONSTRAINT `ScreeningInput_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningOutput`
--
ALTER TABLE `ScreeningOutput`
  ADD CONSTRAINT `ScreeningOutput_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningOutputLattice`
--
ALTER TABLE `ScreeningOutputLattice`
  ADD CONSTRAINT `ScreeningOutputLattice_ibfk_1` FOREIGN KEY (`screeningOutputId`) REFERENCES `ScreeningOutput` (`screeningOutputId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningRank`
--
ALTER TABLE `ScreeningRank`
  ADD CONSTRAINT `ScreeningRank_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ScreeningRank_ibfk_2` FOREIGN KEY (`screeningRankSetId`) REFERENCES `ScreeningRankSet` (`screeningRankSetId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningStrategy`
--
ALTER TABLE `ScreeningStrategy`
  ADD CONSTRAINT `ScreeningStrategy_ibfk_1` FOREIGN KEY (`screeningOutputId`) REFERENCES `ScreeningOutput` (`screeningOutputId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningStrategySubWedge`
--
ALTER TABLE `ScreeningStrategySubWedge`
  ADD CONSTRAINT `ScreeningStrategySubWedge_FK1` FOREIGN KEY (`screeningStrategyWedgeId`) REFERENCES `ScreeningStrategyWedge` (`screeningStrategyWedgeId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningStrategyWedge`
--
ALTER TABLE `ScreeningStrategyWedge`
  ADD CONSTRAINT `ScreeningStrategyWedge_IBFK_1` FOREIGN KEY (`screeningStrategyId`) REFERENCES `ScreeningStrategy` (`screeningStrategyId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `SessionType`
--
ALTER TABLE `SessionType`
  ADD CONSTRAINT `SessionType_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Session_has_Person`
--
ALTER TABLE `Session_has_Person`
  ADD CONSTRAINT `Session_has_Person_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Session_has_Person_ibfk_2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Shipping`
--
ALTER TABLE `Shipping`
  ADD CONSTRAINT `Shipping_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Shipping_ibfk_2` FOREIGN KEY (`sendingLabContactId`) REFERENCES `LabContact` (`labContactId`),
  ADD CONSTRAINT `Shipping_ibfk_3` FOREIGN KEY (`returnLabContactId`) REFERENCES `LabContact` (`labContactId`);

--
-- Constraints for table `ShippingHasSession`
--
ALTER TABLE `ShippingHasSession`
  ADD CONSTRAINT `ShippingHasSession_ibfk_1` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`),
  ADD CONSTRAINT `ShippingHasSession_ibfk_2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`);

--
-- Constraints for table `SpaceGroup`
--
ALTER TABLE `SpaceGroup`
  ADD CONSTRAINT `SpaceGroup_ibfk_1` FOREIGN KEY (`geometryClassnameId`) REFERENCES `GeometryClassname` (`geometryClassnameId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Specimen`
--
ALTER TABLE `Specimen`
  ADD CONSTRAINT `SamplePlateWellToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateWellToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateWellToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateWellToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateWellToSamplePlatePosition` FOREIGN KEY (`samplePlatePositionId`) REFERENCES `SamplePlatePosition` (`samplePlatePositionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SampleToStockSolution` FOREIGN KEY (`stockSolutionId`) REFERENCES `StockSolution` (`stockSolutionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `StockSolution`
--
ALTER TABLE `StockSolution`
  ADD CONSTRAINT `StockSolutionToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StockSolutionToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StockSolutionToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Stoichiometry`
--
ALTER TABLE `Stoichiometry`
  ADD CONSTRAINT `StoichiometryToHost` FOREIGN KEY (`hostMacromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StoichiometryToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Structure`
--
ALTER TABLE `Structure`
  ADD CONSTRAINT `StructureToBlSample` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StructureToCrystal` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StructureToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StructureToProposal` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SubstructureDetermination`
--
ALTER TABLE `SubstructureDetermination`
  ADD CONSTRAINT `SubstructureDetermination_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `SubstructureDetermination_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `SubstructureDetermination_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Subtraction`
--
ALTER TABLE `Subtraction`
  ADD CONSTRAINT `EdnaAnalysisToMeasurement0` FOREIGN KEY (`dataCollectionId`) REFERENCES `SaxsDataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Subtraction_1` FOREIGN KEY (`sampleOneDimensionalFiles`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Subtraction_2` FOREIGN KEY (`bufferOnedimensionalFiles`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SubtractionToAbInitioModel`
--
ALTER TABLE `SubtractionToAbInitioModel`
  ADD CONSTRAINT `substractionToAbInitioModelToAbinitioModel` FOREIGN KEY (`abInitioId`) REFERENCES `AbInitioModel` (`abInitioModelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `substractionToSubstraction` FOREIGN KEY (`subtractionId`) REFERENCES `Subtraction` (`subtractionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SW_onceToken`
--
ALTER TABLE `SW_onceToken`
  ADD CONSTRAINT `SW_onceToken_fk1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  ADD CONSTRAINT `SW_onceToken_fk2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`);

--
-- Constraints for table `UntrustedRegion`
--
ALTER TABLE `UntrustedRegion`
  ADD CONSTRAINT `UntrustedRegion_ibfk_1` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `UserGroup_has_Permission`
--
ALTER TABLE `UserGroup_has_Permission`
  ADD CONSTRAINT `UserGroup_has_Permission_fk1` FOREIGN KEY (`userGroupId`) REFERENCES `UserGroup` (`userGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `UserGroup_has_Permission_fk2` FOREIGN KEY (`permissionId`) REFERENCES `Permission` (`permissionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `UserGroup_has_Person`
--
ALTER TABLE `UserGroup_has_Person`
  ADD CONSTRAINT `userGroup_has_Person_fk1` FOREIGN KEY (`userGroupId`) REFERENCES `UserGroup` (`userGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `userGroup_has_Person_fk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `WorkflowDehydration`
--
ALTER TABLE `WorkflowDehydration`
  ADD CONSTRAINT `WorkflowDehydration_workflowfk_1` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `WorkflowMesh`
--
ALTER TABLE `WorkflowMesh`
  ADD CONSTRAINT `WorkflowMesh_ibfk_2` FOREIGN KEY (`bestImageId`) REFERENCES `Image` (`imageId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `WorkflowMesh_workflowfk_1` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `WorkflowStep`
--
ALTER TABLE `WorkflowStep`
  ADD CONSTRAINT `step_to_workflow_fk` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `XFEFluorescenceSpectrum`
--
ALTER TABLE `XFEFluorescenceSpectrum`
  ADD CONSTRAINT `XFE_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `XFE_ibfk_2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `XFE_ibfk_3` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `XRFFluorescenceMapping`
--
ALTER TABLE `XRFFluorescenceMapping`
  ADD CONSTRAINT `XRFFluorescenceMapping_ibfk1` FOREIGN KEY (`xrfFluorescenceMappingROIId`) REFERENCES `XRFFluorescenceMappingROI` (`xrfFluorescenceMappingROIId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `XRFFluorescenceMapping_ibfk2` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
