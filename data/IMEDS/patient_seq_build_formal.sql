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

select * from ucla.lroutliers where fileid = 11 limit 10;

select id, count(*) from ucla.lroutliers 
where fileid = 9
group by id
having count(*) > 0

select *  from ucla.lroutliers where fileid = 11;
select * from ucla.lroutliers where fileid = 9;

select o11.id, o9.id
from ucla.lroutliers o9 full outer join ucla.lroutliers o11 on o9.id = o11.id
where o9.fileid = 9 and o11.fileid = 11;

select *
from ucla.lroutliers o11 left outer join ucla.lroutliers o9 on o9.id = o11.id
where o9.fileid = 9 and o11.fileid = 11

-----generate outlier patient drug sequence
set search_path to ccae_cdm4;
SELECT distinct de.person_id as person_id, de.drug_era_start_date as date_t, de.drug_concept_id as concept_id--,
				-- CASE WHEN uts.ri>=3  THEN 1 --outlier
				-- 		  WHEN uts.ri<3   THEN 0 --normal
      	-- END
FROM  drug_era de, ucla.lroutliers uts
WHERE de.person_id = uts.id and uts.ri>=3 and uts.fileid = 4
ORDER BY  de.person_id, de.drug_era_start_date,de.drug_concept_id

--save as trainDS_5000_1_preseq_fileid.csv


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

