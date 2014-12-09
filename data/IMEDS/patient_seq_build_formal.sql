select lol.fileid, count(*) , lof.filename
from ucla.LrOutliers lol, ucla.LrOutlierfile lof
where lol.fileid = lof.fileid and lol.ri>=3
group by lol.fileid, lof.filename
order by lof.filename


select lol.fileid, count(*), lof.filename
from ucla.LrOutliers lol, ucla.LrOutlierfile lof
where lol.fileid = lof.fileid and lol.ri>=2
group by lol.fileid, lof.filename
order by lof.filename
set search_path to ccae_cdm4;

select * from ucla.LrOutlierfile

select predict, count(*) from (
select *, case when predictp >=0.5 then 1
							 when predictp <0.5  then 0 end as predict, case when ri>=3 then 1 when ri<3 then 0 end as outlier
from ucla.lroutliers 
where fileid = 13 )
where outlier = 0
group by predict
limit 10;


-----generate outlier patient drug sequence
set search_path to ccae_cdm4;
SELECT distinct de.person_id as person_id, de.drug_era_start_date as date_t, de.drug_concept_id as concept_id--,
				-- CASE WHEN uts.ri>=3  THEN 1 --outlier
				-- 		  WHEN uts.ri<3   THEN 0 --normal
      	-- END
FROM  drug_era de, ucla.lroutliers uts
WHERE de.person_id = uts.id and uts.ri>=3 and uts.fileid = 9
ORDER BY  de.person_id, de.drug_era_start_date,de.drug_concept_id

--save as trainDS_5000_1_preseq_fileid.csv
SELECT person_id, condition_start_date, co_d.* as START_TIME  
FROM condition_occurrence co_d  
WHERE condition_concept_id IN (312327,438438,434376,438447,441579,438170,436706,439693,444406,4108669) AND person_id =998699002 
ORDER BY condition_start_date LIMIT 1

SELECT de.drug_era_start_date as date_t, de.drug_concept_id as concept_id  
FROM drug_era de WHERE de.person_id = 998699002 AND de.drug_era_start_date >= '2007-08-05' ORDER BY de.drug_era_start_date,de.drug_concept_id 

select * from condition_vocab_cache where concept_id = 312327


SELECT distinct uts.id
FROM   ucla.lroutliers uts
WHERE  uts.ri>=3 and uts.fileid = 9
ORDER BY  de.person_id, de.drug_era_start_date,de.drug_concept_id

SELECT person_id, condition_start_date as START_TIME  FROM condition_occurrence co_d  WHERE condition_concept_id IN (316139,319835,439846,443580,40480603,40479192,40480602,443587,40481042,40479576,40481043) AND person_id =25127839302 ORDER BY condition_start_date LIMIT 1

SELECT de.drug_era_start_date as date_t, de.drug_concept_id as concept_id  FROM drug_era de 
WHERE de.person_id = 25127839302 
--AND de.drug_era_start_date >= '2009-01-22' 
ORDER BY de.drug_era_start_date,de.drug_concept_id 

0,8,8,data\IMEDS\CgstHfComorbidDS\outlier\trainDS_10000_1_prol.csv
1,13,1784,data\IMEDS\CgstHfComorbidDS\outlier\trainDS_2000_1_prol.csv
1,9,12,data\IMEDS\CgstHfComorbidDS\outlier\trainDS_5000_1_prol.csv
0,1,4396,data\IMEDS\CgstHfComorbidDS\outlier\trainDS_500_1_prol.csv
0,7,6204,data\IMEDS\CgstHfComorbidDS\outlier\trainDS_10000_10_prol.csv
0,11,6501,data\IMEDS\CgstHfComorbidDS\outlier\trainDS_5000_10_prol.csv
0,10,7399,data\IMEDS\CgstHfComorbidDS\outlier\trainDS_500_10_prol.csv



1,3,40,data\IMEDS\MiComorbidDS\outlier\trainDS_10000_1_prol.csv
1,12,74,data\IMEDS\MiComorbidDS\outlier\trainDS_2000_1_prol.csv
1,4,42,data\IMEDS\MiComorbidDS\outlier\trainDS_5000_1_prol.csv
0,0,1972,data\IMEDS\MiComorbidDS\outlier\trainDS_500_1_prol.csv
0,2,3183,data\IMEDS\MiComorbidDS\outlier\trainDS_10000_10_prol.csv
0,6,3474,data\IMEDS\MiComorbidDS\outlier\trainDS_5000_10_prol.csv
0,5,4060,data\IMEDS\MiComorbidDS\outlier\trainDS_500_10_prol.csv

