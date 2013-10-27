package com.betr.evaluation;

public class SimpleBleuEvaluator extends BleuEvaluator {
	@Override
	public double calculateScore() {
		double precAvg = 0.0; // modified n-gram precisions
        double bleu;

        for (int i = 0; i < MAX_NGRAM; ++i){
            precAvg += (1.0/MAX_NGRAM) * this.clippedHits[i] / (double)this.candLenghts[i];
        }

        bleu = precAvg;
        return bleu;
	}
}
