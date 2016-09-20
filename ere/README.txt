              DEFT Rich ERE Chinese and English Parallel Annotation V1

                             LDC2015E78

                       Linguistic Data Consortium
                             July 31, 2015
  

1. Introduction

DARPA's Deep Exploration and Filtering of Text (DEFT) program aims to 
address remaining capability gaps in state-of-the-art natural language 
processing technologies related to inference, causal relationships and 
anomaly detection. In support of DEFT, LDC is providing source data 
and core resources for system development.

Entity, Relation, and Event (ERE) annotation is a core resource provided
to train developing systems in detecting and coreferencing the three
namesake elements for the task.  This release contains annotation of 172
documents following the Rich ERE annotation guidelines.  In contrast to
Light ERE annotation, Rich ERE annotation expands types and taggability
in Entity, Relation and Event annotation tasks and replaces strict Event
Coref with a more loosely defined Event Hopper annotation.

Annotators completing ERE annotation primarily perform exhaustive
tagging and coreference of valid entities in a provided source
document.  Afterwards, valid relations from the document are further
annotated to supply entity or filler values for the relation
arguments.  Lastly, valid event mentions and event hoppers are
annotated to supply entity or filler values for event arguments.
Relation coreference is an automated process and is not manually
performed by annotators.  Relation mentions that meet the following
criteria are computed as coref:

	-- They have the same type and subtype 
	-- They have the same realis attribute 
	-- If relations are asymmetric, relation1.arg1 == relation2.arg1 
           and relation1.arg2 == relation2.arg2 
	-- If relations are symmetric, relation1.arg1 == relation2.arg1 
             and relation1.arg2 == relation2.arg2 
	   or relation1.arg1 == relation2.arg2 
             and relation1.arg2 == relation2.arg1 
        -- The following three relation type-subtypes are symmetric: 
     		type 		subtype 
     		personalsocial business 
     		personalsocial family 
     		personalsocial unspecified 

(Relation mentions which have a filler as argument are treated as
singletons, because fillers are not coreferenced).

For more information on Rich ERE annotations processes, please refer to 
the guidelines in the ../docs/ directory. 

Source documents are files that were previously released with Light ERE
annotation (LDC2014E114 V2).  Rich ERE annotation was performed on top of
Light ERE annotation.  Source documents are in TXT format (as in Light
ERE releases), and the annotation is in XML format.  Refer to section 4
for annotation format details.

This is the first release of Rich ERE annotations for Parallel Chinese-English 
Discussion Forum data in DEFT.  A revised and extended V2 release of this 
data is to follow which will include additional Chinese annotated files as well 
as the annotated English translation data.  NOTE: the data/translation and data/ere
 directories for the English side are left intentionally empty for this release.  

2. Contents

./README.txt
  This file
	
./docs/
  deft_rich_ere.1.1.dtd --  DTD for ERE xml annotation files.
  chinese_rich_ere_stats.tab
		--ERE annotation statistics by documents
  chinese_mp2df.tab
		--Mapping for each file to original DF thread
  DEFT_RICH_ERE_Annotation_Guidelines_English_Entities_V2.4.pdf
  DEFT_RICH_ERE_Annotation_Guidelines_English_Events_V2.9.pdf
  DEFT_RICH_ERE_Annotation_Guidelines_English_Relations_V4.4.pdf
  DEFT_Rich_ERE_English_ArgumentFiller_V2.1.pdf
                --English Rich ERE annotation guidelines  
  DEFT_RICH_ERE_Annotation_Guidelines_Chinese_Entities_V1.0.pdf
  DEFT_RICH_ERE_Annotation_Guidelines_Chinese_Events_V1.0.pdf
  DEFT_RICH_ERE_Annotation_Guidelines_Chinese_Relations_V1.0.pdf
                --Chinese Rich ERE annotation guidelines

./data/cmn/source/
  This directory contains all of the source documents in TXT format as
  used in ERE annotation.

./data/eng/translation/ [intentionally empty in this release]
  This directory contains all of the translations of Chinese source documents 
  used for English annotations

./data/cmn/ere
  This directory contains the Chinese annotation files.  

./data/eng/ere 	 [intentionally empty in this release]
  This directory contains the English annotation files.

  Note: The IDs for each annotation (entity, entity mention, relation, 
  filler, event hopper, event mention) are unique to each document, not 
  to the entire corpus.

3. Data Profile and Format

Entity / Relation / Event annotation volumes

Lang	Files  Characters Words   Entities (EN mentions)  Fillers Relations  Hoppers (EV mentions)
------------------------------------------------------------------------------------------
CMN	172	128,272	85,541	  6,025 (14,227)	  630	  1,994	     1,145   (1,506)
------------------------------------------------------------------------------------------

ERE annotation files have a .rich_ere.xml extension, and are in XML format.  Word
count for Chinese is based on 1 word = 1.5 Chinese characters. 

For a full description of the elements, attributes, and structure of the ERE
annotation files, please see the DTD in the docs directory of this release.

4. Using the Data

All ERE documents are in the Discussion Forum genre.  To allow for parallel
annotation, material previously translated in the BOLT program was used here.
Translated text in BOLT was in a multi-post (MP) format; within each DF thread
mulitple posts were selected.  These posts were not necessarily continguous.

The files ./docs/chinese_mp2df.tab list where the MPs are drawn from.  Note 
that an MP is an XML fragment rather than a full XML document; it is intended 
to be used as raw text, and uses UNIX-style line termination (line-feed only).

4.1 Offset Calculation

All ERE XML files (file names "*.rich_ere.xml") represent stand-off
annotation of source files (file names "*.mp.txt") and use offsets to
refer to the text extents.

The entity_mention, relation_mention, and event_mention XML elements all
have attributes or contain sub-elements which use character offsets to
identify text extents in the source.  The offset gives the start
character of the text extent; offset counting starts from the initial
character, character 0, of the source document (.mp.txt file) and
includes newlines as well as all characters comprising XML-like tags in
the source data.

4.2 Proper ingesting of XML

Character offsets and lengths for text extents in ERE XML are calculated
based on "raw" MP data, where original (XML-fragment) meta-characters are
escaped.  For example, a reference to the corporation "AT&T" will appear in
MP as "AT&amp;T".  ERE annotation on this string will cite a length of 8
characters (not 4).  This string is stored in the ERE XML file as
"AT&amp;amp;T" because of XML escaping, but returns to "AT&amp;T" when the
ERE XML file is read using an XML parser, as intended.

With regard to white-space characters in annotated text extents, the ERE
XML offset and length are again based on the "raw" MP data, and will
reflect the original quantity of white-space characters.  But in the text
string provided in the ERE XML annotation element, white-space has been
normalized, as described in 4.1 above, and may be shorter.


5. Rich ERE Annotation Pipeline

5.1 Data selection

MP data in this release had been previously vetted for suitability for 
translation during the BOLT program.  In addition, the selection of data
that previously received other annotation (Chinese Treebank, English Parallel 
Chinese Treebank, Word Alignment) was prioritized.

A separate selection stage was used wherein annotators quickly scanned 
documents and ranked them for suitablity for ERE annotation.  The selection 
stage was conducted on the Chinese files. In addition, documents later 
deemed not suitable for ERE annotation were rejected during annotation.  

The documents annotated were part of data annotated and released in
LDC2015E78 V2. 

5.2 Rich ERE Annotation

LDC annotators performed exhaustive ERE annotation on each document.  Each
document is annotated for all tasks by one annotator and then second-pass
annotated by a senior annotator or team leader.  The first pass annotation
is called 1P, and the second pass annotation is called 2P.  For 1P, a
single annotator completes all tasks (entities, relations and events) for a
file.  For 2P, a more experienced senior annotator reviews the first-pass
annotations and corrects any errors they find.  After 2P, additional
corpus-wide quality checks (QC) are conducted on 2P data by the team leader
and selected senior annotators.  Refer to section 5.3 for detailed QC
procedures.

The full annotation process for ERE annotation is represented below:

              1P: entities
                  relations
                  events
                  |
                  V
              2P: entities
                  relations
                  events
                  |
                  V
              QC: entities
                  relation
                  events

Annotation consisted of tagging all mentions of a set of targeted entities,
relations and events, as well as marking coreference for entities and
events (coreference of relations is done automatically, coreference of
events is referred to as "event hopper").  As this data represents the first
large-scale effort to perform Rich ERE annotation, annotators maintained
many ongoing cooperative discussions regarding difficult annotation issues
and specific fringe examples.  Annotators worked together to formulate
consistent approaches to tackle difficult issues and these approaches were
later documented in the Rich ERE annotation guidelines.

Sometimes the discussion forum documents contain quoted texts either from an
external source or from the same document.  The quoted texts are annotated if 
they contain taggable entities, relations or events. 

5.3 Quality Control

After manual quality control on individual files, LDC also conducted a
corpus-wide scan which included:

    -- Scan all time fillers to make sure that all time fillers are normalized
    -- Scan all relation arguments to make sure that only allowable entity
       types were annotated as arguments
    -- Scan all event arguments to make sure that only allowable entity types
       were annotated as arguments
    -- Scan all event hoppers to make sure that event mentions in the same 
       hoppers have same type and sutype value (except for mentions of contact 
       and transaction type, which only need to agree on type level)

Additional manual scans to review outliers on all annotation will be performed
in an updated V2 release. 

6. Data Validation

For all text extent references, it was verified that the combination of docid, 
offset, and length was a valid reference to a string identical to content of 
XML text extent element.

 - Verified trigger text extent references valid
 - Verified arg text extent references valid
 - Verified entity mention text extent references valid
 - Verified filler text extent references valid
 - Verified each ERE kits in delivery received annotations

Checks were also performed to identify and correct systematic errors that
occurred for certain event subtypes and argument types.

7. Known Issues

Strict Event coref of event mentions within a document (as contrasted to
Event Hopper) is not labeled.

8. Contact Information

  Stephanie Strassel <strassel@ldc.upenn.edu>  PI
  Jonathan Wright    <jdwright@ldc.upenn.edu>  Technical oversight
  Zhiyi Song         <zhiyi@ldc.upenn.edu>     ERE annotation project manager
  Tom Riese          <riese@ldc.upenn.edu>     ERE annotation lead annotator
  Ann Bies           <bies@ldc.upenn.edu>      ERE annotation consultant
  Justin Mott        <jmott@ldc.upenn.edu>     ERE annotation lead annotator
-------------------

README Update Log
  Copied from LDC2015E68 V1
  Updated: Zhiyi Song, May 26, 2015
  Updated: Dave Graff, May 28, 2015
  Updated: Zhiyi Song, June 30, 2015
  Updated: Justin Mott, July 30, 2015
  Updated: Zhiyi Song, July 30, 2015
