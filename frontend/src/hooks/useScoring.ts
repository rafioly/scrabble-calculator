import { useState, useEffect } from 'react';

type ScoringRulesType = { [key: string]: number };

export const useScoring = (tiles: string[]) => {
    const [scoringRules, setScoringRules] = useState<ScoringRulesType>({});
    const [score, setScore] = useState<number>(0);
    const [error, setError] = useState<string | null>(null);

    // Effect to fetch scoring rules on initial load
    useEffect(() => {
        const fetchScoringRules = async () => {
            try {
                const response = await fetch('/api/scoring-rules');
                const data = await response.json();
                if (data.success) {
                    setScoringRules(data.data);
                } else {
                    throw new Error(data.message || 'Failed to fetch scoring rules.');
                }
            } catch (e) {
                setError(e instanceof Error ? e.message : 'An unknown error occurred.');
            }
        };
        fetchScoringRules();
    }, []);

    // Effect to recalculate score whenever tiles or scoring rules change
    useEffect(() => {
        const currentWord = tiles.join('').toUpperCase();
        let currentScore = 0;
        for (const char of currentWord) {
            currentScore += scoringRules[char] || 0;
        }
        setScore(currentScore);
    }, [tiles, scoringRules]);

    return { score, scoringRules, error };
};
