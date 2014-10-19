select lol.fileid, count(*), lof.filename
from ucla.LrOutliers lol, ucla.LrOutlierfile lof
where lol.fileid = lof.fileid and lol.ri>3
group by lol.fileid, lof.filename

set search_path to ccae_cdm4;

SELECT distinct de.person_id as person_id, de.drug_era_start_date as date_t, de.drug_concept_id as concept_id --,
				 --CASE WHEN uts.ri>=3  THEN 1 --outlier
				 --		  WHEN uts.ri<3   THEN 0 --normal
      	 --END
FROM  drug_era de, ucla.lroutliers uts
WHERE de.person_id = uts.id and uts.fileid = 4
ORDER BY  de.person_id, de.drug_era_start_date,de.drug_concept_id

--save as trainDS_5000_1_preseq_fileid.csv
