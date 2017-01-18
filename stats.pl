#!/usr/bin/perl
use strict;

# Perl script used to format performance test results
# Author : Alexis "alex205" Girardi

my $numberOfTestsOk = 0;
my $totalTests = 0;

#Dijkstra stats
my $dijkstraTests = 0;
my $dijkstraTotDist = 0;
my $dijkstraTotTime = 0;
my $dijkstraTotHeap = 0;
my $dijkstraTotNodes = 0;
my $dijkstraTotExecTime = 0;

my $dijkstraAVGHeapDist = 0;
my $dijkstraAVGNodesDist = 0;
my $dijkstraAVGExecTimeDist = 0;
my $dijkstraAVGHeapTime = 0;
my $dijkstraAVGNodesTime = 0;
my $dijkstraAVGExecTimeTime = 0;


#A* stats
my $astarTests = 0;
my $astarTotDist = 0;
my $astarTotTime = 0;
my $astarTotHeap = 0;
my $astarTotNodes = 0;
my $astarTotExecTime = 0;

my $astarAVGHeapDist = 0;
my $astarAVGNodesDist = 0;
my $astarAVGExecTimeDist = 0;
my $astarAVGHeapTime = 0;
my $astarAVGNodesTime = 0;
my $astarAVGExecTimeTime = 0;

sub readfile {
  my ($input) = @_;
  my @buffer;

  open(my $fh, '<:encoding(UTF-8)', $input) or die "Impossible d'ouvrir le fichier $input !";
  while(<$fh>) {
    chomp;
    $totalTests++;
    push(@buffer, $_) if not /.+,.+,.+,.+,0\.0,.+/
  }
  return @buffer;
}

sub parsefile {
  my ($refBuffer) = @_;
  foreach my $l (@{$refBuffer}) {
    my ($execTime, $maxHeap, $nodes, $org, $dest, $type, $dot, $totDist, $totTime) = split(',', $l);
    $dijkstraTests++ if $type == 0;
    $dijkstraTotDist += $totDist if $type == 0 and $dot == 0;
    $dijkstraTotTime += $totTime if $type == 0 and $dot == 1;
    $dijkstraTotHeap += $maxHeap if $type == 0;
    $dijkstraTotNodes += $nodes if $type == 0;
    $dijkstraTotExecTime += $execTime if $type == 0;
    $astarTests++ if $type == 1;
    $astarTotDist += $totDist if $type == 1 and $dot == 0;
    $astarTotTime += $totTime if $type == 1 and $dot == 1;
    $astarTotHeap += $maxHeap if $type == 1;
    $astarTotNodes += $nodes if $type == 1;
    $astarTotExecTime += $execTime if $type == 1;
  }
}

sub computeStats() {
  $dijkstraAVGHeapDist = ($dijkstraTotHeap / $dijkstraTotDist) *100;
  $dijkstraAVGHeapTime  = ($dijkstraTotHeap / $dijkstraTotTime) * 60;
  $dijkstraAVGNodesDist = ($dijkstraTotNodes / $dijkstraTotDist) * 100;
  $dijkstraAVGNodesTime = ($dijkstraTotNodes / $dijkstraTotTime) * 60;
  $dijkstraAVGExecTimeDist = ($dijkstraTotExecTime / $dijkstraTotDist) * 100;
  $dijkstraAVGExecTimeTime = ($dijkstraTotExecTime / $dijkstraTotTime) * 60;

  $astarAVGHeapDist = ($astarTotHeap / $astarTotDist) *100;
  $astarAVGHeapTime  = ($astarTotHeap / $astarTotTime) * 60;
  $astarAVGNodesDist = ($astarTotNodes / $astarTotDist) * 100;
  $astarAVGNodesTime = ($astarTotNodes / $astarTotTime) * 60;
  $astarAVGExecTimeDist = ($astarTotExecTime / $astarTotDist) * 100;
  $astarAVGExecTimeTime = ($astarTotExecTime / $astarTotTime) * 60;
}

if($#ARGV+1 < 1) {
  die("Usage : ./stats.pl input[.txt] [output.tex]");
}

my ($input, $output) = @ARGV;
if(not defined $output) {
  $output = $input;
  $output =~ s/\.txt$//;
  $output .= ".tex";
}

my @buffer = readfile($input);

$numberOfTestsOk = $#buffer+1;
parsefile(\@buffer);
print "tot dist $dijkstraTotDist\n";
computeStats();


my $errTest =  $totalTests-$numberOfTestsOk;
print "$numberOfTestsOk tests dont $errTest erronés\n";
print "\n--- DIJKSTRA --- \n";
print "Nombre max moyen d'éléments dans le tas pour 100km $dijkstraAVGHeapDist \n";
print "Nombre max moyen d'éléments dans le tas pour 1min $dijkstraAVGHeapTime \n";
print "Nombre de sommets visités moyens pour 100km $dijkstraAVGNodesDist\n";
print "Nombre de sommets visités moyens pour 1min $dijkstraAVGNodesTime\n";
print "Temps d'exécution moyen pour 100km $dijkstraAVGExecTimeDist s\n";
print "Temps d'exécution moyen pour 1min $dijkstraAVGExecTimeTime s\n";
print "\n--- ASTAR --- \n";
print "Nombre max moyen d'éléments dans le tas pour 100km $astarAVGHeapDist \n";
print "Nombre max moyen d'éléments dans le tas pour 1min $astarAVGHeapTime \n";
print "Nombre de sommets visités moyens pour 100km $astarAVGNodesDist\n";
print "Nombre de sommets visités moyens pour 1min $astarAVGNodesTime\n";
print "Temps d'exécution moyen pour 100km $astarAVGExecTimeDist s\n";
print "Temps d'exécution moyen pour 1min $astarAVGExecTimeTime s\n";
