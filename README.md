# BETest

## Instructions

In this repo, you'll find two files, Workbook2.csv and Workbook2.prn. These files need to be converted to a HTML format by the code you deliver. Please consider your work as a proof of concept for a system that can keep track of credit limits from several sources.

This repository is created specially for you, so you can push anything you like. Please update this README to provide instructions, notes and/or comments for us.


Copyright (C) 2001 - 2018 by Marktplaats BV an Ebay company. All rights reserved.

# Solution

1. We can process large inputs thanks to `fs2` streams and `Cats effects`
2. Invalid inputs will be ignored 
3. Program is able to process different several sources at once (both `CSV` and `PRN`)

**Run**

In sbt shell

```bash
run <path to source1> [<path to source2> ...[<path to sourceN>]] <path to result>
```
		
**Example** 

```bash
sbt:betest> run Workbook2.csv Workbook2.prn report.html
```

Please check `report.html` for result.
