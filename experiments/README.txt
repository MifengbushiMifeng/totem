How to create an experiment:

 * create an experiment directory
   $ mkdir experiment1
   $ cd experiment1

 * create or copy a file 'network.xml'
   $ cp /some/path/to/someDomain.xml network.xml

 * make sure the result files are there as 'results-ALGONAME-results.txt'
   $ cp /some/path/to/som_results_file.txt results-ALGONAME-results.txt
 
 * create the experiment description (<numberOfTimes> <mean1> <stddev1> <mean2> <stddev2> <coinflip>)
   $ echo 100 100.0 50.0 10.0 10.0 0.8 >experiment.txt
 
 * run the experiment
   $ /path/to/scripts/run-experiment
 
 * your results are in results.csv

 * if you want to clean the folder, run
   $ /path/to/run-experiment clean

 * if you want to convert a result txt file to the XML format for Totem, run
   $ /path/to/scripts/convert-resulttxt-to-xml results.txt results.xml
