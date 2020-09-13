# Assignment 1 Makefile
# Gift Mugweni
# 23 August 2020

# The top level directory
.SUFFIXES: .java .class
SRCDIR=FlowSkeleton
BINDIR=bin
OUTDIR=output
OUTDOCS=docs
DATA=data

$(BINDIR)/%.class:$(SRCDIR)/%.java
	javac -d $(BINDIR)/ -cp $(BINDIR) $<
	
CLASSES=Terrain.class \
		Water.class \
		WaterPainter.class \
		FlowController.class \
		FlowPanel.class \
		Flow.class

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class
	rm $(OUTDIR)/*.csv

doc:
	rm $(OUTDOCS)/*
	javadoc -d $(OUTDOCS)/ $(SRCDIR)/*.java $<
