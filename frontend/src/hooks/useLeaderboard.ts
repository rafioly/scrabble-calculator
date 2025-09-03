import { useState } from 'react';

type LeaderboardEntry = { word: string; score: number };

export const useLeaderboard = () => {
    const [leaderboard, setLeaderboard] = useState<LeaderboardEntry[]>([]);
    const [isLeaderboardOpen, setLeaderboardOpen] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleViewScores = async () => {
        try {
            const response = await fetch('/api/leaderboards?top=10');
            const data = await response.json();
            if (data.success) {
                setLeaderboard(data.data);
                setLeaderboardOpen(true);
                setError(null);
            } else {
                throw new Error(data.message || 'Failed to fetch leaderboard.');
            }
        } catch (e) {
            setError(e instanceof Error ? e.message : 'An unknown error occurred.');
        }
    };

    const handleSaveScore = async (word: string) => {
        if (!word) {
            setError('Please enter a word before saving.');
            return;
        }

        try {
            const response = await fetch('/api/leaderboards', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ word }),
            });
            const data = await response.json();

            if (response.ok && data.success) {
                setSuccess(`Successfully saved word: "${word}" with score: ${data.data.score}`);
                setError(null);
            } else {
                throw new Error(data.message || 'Failed to save score.');
            }
        } catch (e) {
            setSuccess(null);
            setError(e instanceof Error ? e.message : 'An unknown error occurred.');
        }
    };

    const closeLeaderboard = () => setLeaderboardOpen(false);

    const resetMessages = () => {
        setError(null);
        setSuccess(null);
    };

    return { leaderboard, isLeaderboardOpen, error, success, handleViewScores, handleSaveScore, closeLeaderboard, resetMessages };
};
