Measuring the impact of lineage tracking
========================================

| Author:      | Sylvain Hallé and Hugo Tremblay |
| Contact:     | shalle@acm.org                  |
| Version:     | 1.0                             |
| Date:        | 2021-04-24                      |

[Petit Poucet](https://github.com/liflab/petitpoucet) is Java
library that allows users to create function circuits over which
*lineage queries* can then be addressed.

This benchmark measures the impact on performance (processing time and
memory) of the lineage features. It does so by evaluating various function
circuits on multiple types of inputs.

The results produced by this lab are discussed in the following publication:

- S. Hallé, H. Tremblay. (2021). Foundations of Fine-Grained Explainability.
  Proceedings of the 33rd International Conference on Computer-Aided
  Verification (CAV 2021).

A version of this repository has been submitted as an artifact for the
conference.

Instructions on using this archive
----------------------------------

This archive contains an instance of LabPal, an environment for running
experiments on a computer and collecting their results in a user-friendly way.
The author of this archive has set up a set of experiments, which typically
involve running scripts on input data, processing their results and displaying
them in tables and plots. LabPal is a library that wraps around these
experiments and displays them in an easy-to-use web interface. The principle
behind LabPal is that all the necessary code, libraries and input data should be
bundled within a single self-contained JAR file, such that anyone can download
and *easily* reproduce someone else's experiments. Detailed instructions can be
found on the LabPal website, [https://liflab.github.io/labpal]

Running LabPal
--------------

In order to run LabPal, you need to have Java properly installed. Java can be
freely downloaded and installation instructions are easy to find on the web.
If you want to see any plots associated to the experiments, you need to have
[GnuPlot](http://gnuplot.info) installed and available from the command line
by typing `gnuplot`.

This archive should contain a single runnable JAR file; suppose it is called
`my-lab.jar`. To start the lab and use its web interface, type at the command
line:

    java -jar my-lab.jar

You should see something like this:

    LabPal 2.8 - A versatile environment for running experiments
    (C) 2014-2017 Laboratoire d'informatique formelle
    Université du Québec à Chicoutimi, Canada
    Please visit http://localhost:21212/index to run this lab
    Hit Ctrl+C in this window to stop

Open your web browser, and type `http://localhost:21212/index` in the address
bar. This should lead you to the main page of LabPal's web control panel.
(Note that the machine running LabPal does not need to have a web browser.
You can open a browser in another machine, and replace `localhost` by the IP
address of the former.)

Using the web interface
-----------------------

The main page should give you more details about the actual experiments that
this lab contains. Here is how you typically use the LabPal web interface.

1. Go to the Experiments page.
2. Select some experiments in the list by clicking on the corresponding
   checkbox.
3. Click on the "Add to assistant" button to queue these experiments
4. Go to the Assistant page
5. Click on the "Start" button. This will launch the execution of each
   experiment one after the other.
6. At any point, you can look at the results of the experiments that have run so
   far. You can do so by:
   - Going to the Plots or the Tables page and see the plots and tables created
     for this lab being updated in real time
   - Going back to the list of experiments, clicking on one of them and get the
     detailed description and data points that this experiment has generated
7. Once the assistant is done, you can export any of the plots and tables to a
   file, or the raw data points by using the Export button in the Status page.

Please refer to the [LabPal website](https://liflab.github.io/labpal)
or to the Help page within the web interface for more information about
LabPal's functionalities.

Disclaimer
----------

The LabPal *library* was written by Sylvain Hallé, Associate Professor at
Université du Québec à Chicoutimi, Canada. However, the *experiments* contained 
in this specific lab instance and the results they produce are the sole
responsibility of their author.

<!-- :maxLineLen=80: -->